package com.github.artsiomshshshsk.findproject.dto;

import lombok.Builder;

@Builder
public record RoleResponse (
      Long id,
      String name,
      UserResponse assignedUser
){}
