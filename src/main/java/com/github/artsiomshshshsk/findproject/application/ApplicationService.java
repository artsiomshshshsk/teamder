package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.exception.ApplicationCreationException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.project.ProjectService;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.utils.FileType;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
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
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ProjectService projectService;
    private final FileUploadService fileUploadService;

    private final ApplicationMapper applicationMapper;



    public void createApplication(ApplicationRequest applicationRequest, User user, Long id) {
        if(user.getResumeURL() == null && applicationRequest.getCv() == null){
            throw new ApplicationCreationException("Application won't be created without cv");
        }
        Project project = projectService.findById(id);
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

        if(role.getAssignedUser() != null){
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
        applicationRepository.save(application);
    }


    public Page<ApplicationResponse> getAllApplications(User user, Long projectId, Pageable pageable) {
        Project project = projectService.findById(projectId);
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
