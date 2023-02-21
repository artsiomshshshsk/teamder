package com.github.artsiomshshshsk.findproject.service;


import com.github.artsiomshshshsk.findproject.domain.*;
import com.github.artsiomshshshsk.findproject.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.dto.catalog.CatalogProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.mapper.ProjectMapper;
import com.github.artsiomshshshsk.findproject.repository.ApplicationRepository;
import com.github.artsiomshshshsk.findproject.repository.ProjectRepository;
import com.github.artsiomshshshsk.findproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ApplicationRepository applicationRepository;

    private final FileUploadService fileUploadService;

    public ProjectResponse findProjectById(Long id) {
        return projectMapper.toProjectResponse(findById(id));
    }

    private Project findById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Project with id %s not found", id)));
    }

    public static Specification<Project> hasStatus(ProjectStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public Page<CatalogProjectResponse> getProjectCatalog(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(hasStatus(ProjectStatus.RECRUITING),pageable);
        return projects.map(projectMapper::toCatalogProjectResponse);
    }


    public ProjectResponse createProject(User user, ProjectRequest projectRequest) {
        Project project = projectMapper.toProject(user,projectRequest);
        project.setPublishedAt(LocalDateTime.now());
        if(projectRequest.roles().isEmpty()){
            project.setStatus(ProjectStatus.IN_DEVELOPMENT);
        }else{
            project.setStatus(ProjectStatus.RECRUITING);
        }
        user.addProject(project);
        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    public void createApplication(ApplicationRequest applicationRequest, User user, Long id) {
        if(user.getResumeURL() == null && applicationRequest.cv() == null){
            throw new IllegalStateException("Application won't be created without cv");
        }
        Project project = findById(id);
        if(project.getOwner().equals(user)){
            throw new IllegalStateException("You can't apply for your own project");
        }

        if(project.hasApplicant(user)){
            throw new IllegalStateException("You have already applied for your this project.");
        }

        if(!project.hasOpenedRole(applicationRequest.roleRequest())){
            throw new IllegalStateException("There is no such opened role.");
        }

        Application application = Application.builder()
                .applicant(user)
                .applicationMessage(applicationRequest.applicationMessage())
                .project(project)
                .roleRequest(applicationRequest.roleRequest())
                .status(ApplicationStatus.WAITING_FOR_REVIEW)
                .submittedAt(LocalDateTime.now())
                .build();

        if(applicationRequest.cv() != null){
            application.setResumeURL(fileUploadService.uploadFile(applicationRequest.cv(), FileType.CV));
        }else{
            application.setResumeURL(user.getResumeURL());
        }

        user.addApplication(application);
        project.addApplication(application);
        applicationRepository.save(application);
    }
}
