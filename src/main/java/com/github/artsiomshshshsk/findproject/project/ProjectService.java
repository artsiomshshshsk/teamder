package com.github.artsiomshshshsk.findproject.project;


import com.github.artsiomshshshsk.findproject.application.*;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.project.dto.*;
import com.github.artsiomshshshsk.findproject.exception.ApplicationCreationException;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.utils.UploadValidationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ApplicationMapper applicationMapper;
    private final ApplicationRepository applicationRepository;
    private final UploadValidationService uploadValidationService;

    public ProjectProfileResponse getProjectProfile(Long id) {
        return projectMapper.toProjectProfileResponse(findById(id));
    }

    public Project findById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Project with id %s not found", id)));
    }


    public Page<CatalogProjectResponse> getProjectCatalog(Pageable pageable, String searchQuery) {
        if(searchQuery == null || searchQuery.isEmpty())
            return projectRepository.findAllByIsVisibleTrue(pageable).map(projectMapper::toCatalogProjectResponse);
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(projectMapper::toCatalogProjectResponse);
    }


    public ProjectResponse createProject(User user, ProjectRequest projectRequest) {
        Project project = projectMapper.toProject(user,projectRequest);
        project.setPublishedAt(LocalDateTime.now());
        project.setIsVisible(!projectRequest.roles().isEmpty());
        user.addProject(project);
        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    public AppliedResponse applyForProject(Long projectId, User user, ApplicationRequest applicationRequest){

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

        if(Boolean.FALSE.equals(project.hasOpenedRoleWithName(role.getName()))){
            throw new ApplicationCreationException(String.format("You can't apply for role: %s because it is occupied",
                    applicationRequest.getRoleRequest()));
        }

        Application application = Application.builder()
                .applicant(user)
                .message(applicationRequest.getApplicationMessage())
                .project(project)
                .roleRequest(role)
                .status(ApplicationStatus.WAITING_FOR_REVIEW)
                .applicationDate(LocalDateTime.now())
                .build();

        if(applicationRequest.getCv() != null){
            application.setResumeURL(uploadValidationService.validateAndUploadCv(applicationRequest.getCv()));
        }else{
            application.setResumeURL(user.getResumeURL());
        }

        user.addApplication(application);
        project.addApplication(application);

        Application savedApplication = applicationRepository.saveAndFlush(application);
        return applicationMapper.toAppliedResponse(savedApplication);
    }
    public Page<ProjectApplicationResponse> getAllApplications(User user, Long projectId, Pageable pageable) {
        Project project = findById(projectId);
        if(!project.getOwner().equals(user)){
            throw new UnauthorizedAccessException("You don't have an access to applications for project that you don't own");
        }
        List<Application> applications = applicationRepository.findAllByProjectId(projectId).stream().filter(
                application -> application.getStatus().equals(ApplicationStatus.WAITING_FOR_REVIEW)).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), applications.size());
        List<ProjectApplicationResponse> applicationResponses = new ArrayList<>();

        for (int i = start; i < end; i++) {
            User applicant = applications.get(i).getApplicant();
            Application application = applications.get(i);
            applicationResponses.add(ProjectApplicationResponse.builder()
                            .id(application.getId())
                            .username(applicant.getUsername())
                            .contact(applicant.getContact() != null ? applicant.getContact() : applicant.getEmail())
                            .applicantMessage(application.getMessage())
                            .role(application.getRoleRequest().getName())
                            .resumeURL(application.getResumeURL())
                            .applicationDate(application.getApplicationDate())
                    .build());
        }
        return new PageImpl<>(applicationResponses, pageable, applications.size());
    }

    public void deleteProject(User user, Long projectId) {
        Project project = findById(projectId);
        if(!project.getOwner().equals(user)){
            throw new UnauthorizedAccessException("You don't have an access to delete project that you don't own");
        }
        projectRepository.delete(project);
    }
}
