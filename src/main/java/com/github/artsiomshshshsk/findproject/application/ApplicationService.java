package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.application.dto.UpdateApplicationRequest;
import com.github.artsiomshshshsk.findproject.exception.ApplicationDecisionException;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.utils.UploadType;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
import com.github.artsiomshshshsk.findproject.utils.UploadValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UploadValidationService uploadValidationService;
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
            removeResumeFromApplication(application);
            application.setResumeURL(uploadValidationService.validateAndUploadCv(cv));
        }

        return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }


    private void removeResumeFromApplication(Application application) {
        if(application.getResumeURL() != null && !application.getApplicant().cvIsUsed(application.getResumeURL())){
            uploadValidationService.deleteFile(application.getResumeURL());
        }
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

        removeResumeFromApplication(application);
        application.getProject().removeApplication(application);
        application.getApplicant().getApplications().remove(application);
        applicationRepository.delete(application);
    }
}
