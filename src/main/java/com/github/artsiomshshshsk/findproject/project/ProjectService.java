package com.github.artsiomshshshsk.findproject.project;


import com.github.artsiomshshshsk.findproject.application.*;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.project.dto.CatalogProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ApplicationCreationException;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.role.dto.RoleRequest;
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
import java.util.Set;
import java.util.stream.Collectors;

import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final FileUploadService fileUploadService;
    private final ApplicationMapper applicationMapper;
    private final ApplicationRepository applicationRepository;

    public ProjectResponse findProjectById(Long id) {
        return projectMapper.toProjectResponse(findById(id));
    }

    public Project findById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Project with id %s not found", id)));
    }


    public Page<CatalogProjectResponse> getProjectCatalog(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByIsVisibleTrue(pageable);
        return projects.map(projectMapper::toCatalogProjectResponse);
    }


    public ProjectResponse createProject(User user, ProjectRequest projectRequest) {
//        Set<String> names = projectRequest.roles().stream()
//                .map(RoleRequest::name)
//                .collect(Collectors.toSet());
//        if(names.size() != projectRequest.roles().size()){
//            throw new ApplicationCreationException("Project can't have two roles with one name")
//        }


        Project project = projectMapper.toProject(user,projectRequest);
        project.setPublishedAt(LocalDateTime.now());
        project.setIsVisible(!projectRequest.roles().isEmpty());
        user.addProject(project);
        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    public ApplicationResponse applyForProject(Long projectId, User user, ApplicationRequest applicationRequest){
        if(user.getResumeURL() == null && applicationRequest.getCv() == null){
            throw new ApplicationCreationException("Application won't be created without cv");
        }
        Project project = findById(projectId);
        if(project.getOwner().equals(user)){
            throw new ApplicationCreationException("You can't apply for your own project");
        }

        if(project.hasApplicant(user)){
            throw new ApplicationCreationException("You have already applied for your this project.");
        }


        Role role = project.findRoleByName(applicationRequest.getRoleRequest()).orElseThrow(
                () -> new ApplicationCreationException(String.format("There is no role: %s in the project",
                        applicationRequest.getRoleRequest()))
        );

        if(!project.hasOpenedRoleWithName(role.getName())){
            throw new ApplicationCreationException(String.format("You can't apply for role: %s because it is occupied",
                    applicationRequest.getRoleRequest()));
        }

        Application application = Application.builder()
                .applicant(user)
                .message(applicationRequest.getRoleRequest())
                .project(project)
                .roleRequest(role)
                .status(ApplicationStatus.WAITING_FOR_REVIEW)
                .applicationDate(LocalDateTime.now())
                .build();

        if(applicationRequest.getCv() != null){
            application.setResumeURL(fileUploadService.uploadFile(applicationRequest.getCv(), FileType.CV));
        }else{
            application.setResumeURL(user.getResumeURL());
        }

        user.addApplication(application);
        project.addApplication(application);
        return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }
    public Page<ApplicationResponse> getAllApplications(User user, Long projectId, Pageable pageable) {
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
