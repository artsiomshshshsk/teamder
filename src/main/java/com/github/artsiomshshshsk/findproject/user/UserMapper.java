package com.github.artsiomshshshsk.findproject.user;

import com.github.artsiomshshshsk.findproject.application.Application;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.user.dto.DashboardApplicationResponse;
import com.github.artsiomshshshsk.findproject.user.dto.Participation;
import com.github.artsiomshshshsk.findproject.user.dto.UserResponse;
import com.github.artsiomshshshsk.findproject.security.dto.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest registerRequest);

    default Participation toParticipation(Application application){
        return Participation.builder()
                .projectId(application.getProject().getId())
                .projectTitle(application.getProject().getName())
                .shortDescription(application.getProject().getShortDescription())
                .role(application.getRoleRequest().getName())
                .isOwner(false)
                .build();
    }


    default Participation toParticipation(Project project){
        return Participation.builder()
                .projectId(project.getId())
                .projectTitle(project.getName())
                .shortDescription(project.getShortDescription())
                .role(project.getRoles().stream()
                        .filter(role -> role.getAssignedUser().getId().equals(project.getOwner().getId()))
                        .findFirst()
                        .orElseThrow(() -> new UnauthorizedAccessException("User is not owner of the project"))
                        .getName())
                .isOwner(true)
                .build();
    }


    default DashboardApplicationResponse toDashboardApplicationResponse(Application application){
        return DashboardApplicationResponse.builder()
                .id(application.getId())
                .projectName(application.getProject().getName())
                .applicantMessage(application.getMessage())
                .resumeURL(application.getResumeURL())
                .role(application.getRoleRequest().getName())
                .applicationDate(application.getApplicationDate())
                .status(application.getStatus())
                .build();
    }

}
