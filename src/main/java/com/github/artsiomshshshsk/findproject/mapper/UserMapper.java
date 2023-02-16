package com.github.artsiomshshshsk.findproject.mapper;

import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.UserResponse;
import com.github.artsiomshshshsk.findproject.security.auth.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest registerRequest);
}
