package com.github.artsiomshshshsk.findproject.user;

import com.github.artsiomshshshsk.findproject.user.dto.UserResponse;
import com.github.artsiomshshshsk.findproject.security.dto.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest registerRequest);
}
