package com.github.artsiomshshshsk.findproject.user;

import com.github.artsiomshshshsk.findproject.application.Application;
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

}
