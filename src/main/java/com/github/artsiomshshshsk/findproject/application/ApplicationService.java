package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.application.dto.UpdateApplicationRequest;
import com.github.artsiomshshshsk.findproject.exception.ApplicationCreationException;
import com.github.artsiomshshshsk.findproject.exception.ApplicationDecisionException;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.project.ProjectRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final FileUploadService fileUploadService;
    private final ApplicationMapper applicationMapper;



    public Application findById(Long id){
        return applicationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Application with id: %s not found", id))
        );
    }

    public ApplicationResponse processApplicationDecision(User user, Long applicationId, ApplicationDecision decision) {
        Application application = findById(applicationId);

        if(!application.getProjectOwner().equals(user)){
            throw new UnauthorizedAccessException("You can't process application decision for other user's project");
        }

        if(application.getStatus() != ApplicationStatus.WAITING_FOR_REVIEW){
            throw new ApplicationDecisionException("You can't change the status of the application that is" +
                    " not in waiting for review status");
        }

        if(decision == ApplicationDecision.ACCEPTED){
            Role role = application.getRoleRequest();
            if(role.getAssignedUser() != null){
                throw new ApplicationDecisionException(String.format("You can't accept application for role: %s " +
                                "because it is occupied",
                        role.getName()));
            }
            role.setAssignedUser(application.getApplicant());
            application.setStatus(ApplicationStatus.ACCEPTED);
        }else {
            application.setStatus(ApplicationStatus.REJECTED);
        }

        return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }

    public ApplicationResponse updateApplication(User user, Long applicationId, UpdateApplicationRequest updateApplicationRequest) {
        Application application = findById(applicationId);

        if(!application.getApplicant().equals(user)){
            throw new UnauthorizedAccessException("You can't update application for other user");
        }

        if(application.getStatus() != ApplicationStatus.WAITING_FOR_REVIEW){
            throw new ApplicationDecisionException("You can't update the application that is" +
                    " not in waiting for review status");
        }

        String roleName = updateApplicationRequest.getRoleRequest();
        if(roleName != null){
            application.setRoleRequest(application.getProject().findOpenedRoleByName(roleName).orElseThrow(
                    () -> new ResourceNotFoundException(String.format("Role with name: %s not found", roleName))
            ));
        }

        String applicationMessage = updateApplicationRequest.getApplicationMessage();
        if(applicationMessage != null){
            application.setMessage(applicationMessage);
        }

        MultipartFile cv = updateApplicationRequest.getCv();
        if(cv != null){
            application.setResumeURL(fileUploadService.uploadFile(cv, FileType.CV));
        }

        return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }

    public void removeApplication(User user, Long applicationId) {
        Application application = findById(applicationId);

        if(!application.getApplicant().equals(user)){
            throw new UnauthorizedAccessException("You can't remove application for other user");
        }

        if(application.getStatus() != ApplicationStatus.WAITING_FOR_REVIEW){
            throw new ApplicationDecisionException("You can't remove the application that is" +
                    " not in waiting for review status");
        }
        application.getProject().removeApplication(application);
        applicationRepository.delete(application);
    }
}
