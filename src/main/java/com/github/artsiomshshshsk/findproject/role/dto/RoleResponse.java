package com.github.artsiomshshshsk.findproject.role.dto;

import com.github.artsiomshshshsk.findproject.user.dto.UserResponse;
import lombok.Builder;

@Builder
public record RoleResponse (
      Long id,
      String name,
      UserResponse assignedUser
){}
