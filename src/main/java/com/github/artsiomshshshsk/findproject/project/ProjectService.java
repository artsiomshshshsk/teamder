package com.github.artsiomshshshsk.findproject.project;


import com.github.artsiomshshshsk.findproject.application.Application;
import com.github.artsiomshshshsk.findproject.application.ApplicationStatus;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.project.dto.CatalogProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ApplicationCreationException;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.application.ApplicationMapper;
import com.github.artsiomshshshsk.findproject.application.ApplicationRepository;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.utils.FileType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ApplicationRepository applicationRepository;
    private final FileUploadService fileUploadService;
    private final ApplicationMapper applicationMapper;

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
            throw new ApplicationCreationException("Application won't be created without cv");
        }
        Project project = findById(id);
        if(project.getOwner().equals(user)){
            throw new ApplicationCreationException("You can't apply for your own project");
        }

        if(project.hasApplicant(user)){
            throw new ApplicationCreationException("You have already applied for your this project.");
        }

        Role role = project.findRoleByName(applicationRequest.roleRequest()).orElseThrow(
                () -> new ApplicationCreationException(String.format("There is no role: %s in the project",
                        applicationRequest.roleRequest()))
        );

        if(role.getAssignedUser() != null){
            throw new ApplicationCreationException(String.format("You can't apply for role: %s because it is occupied",
                    applicationRequest.roleRequest()));
        }

        Application application = Application.builder()
                .applicant(user)
                .message(applicationRequest.applicationMessage())
                .project(project)
                .roleRequest(role)
                .status(ApplicationStatus.WAITING_FOR_REVIEW)
                .applicationDate(LocalDateTime.now())
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



    public Page<ApplicationResponse> getAllApplications(User user,Long projectId, Pageable pageable) {
        Project project = findById(projectId);
        if(!project.getOwner().equals(user)){
            throw new UnauthorizedAccessException("You don't have an access to applications for project that you don't own");
        }

        List<Application> applications = project.getApplications();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), applications.size());
        List<ApplicationResponse> applicationResponses = new ArrayList<>();

        for (int i = start; i < end; i++) {
            applicationResponses.add(applicationMapper.toApplicationResponse(applications.get(i)));
        }
        return new PageImpl<>(applicationResponses, pageable, applications.size());
    }


}
