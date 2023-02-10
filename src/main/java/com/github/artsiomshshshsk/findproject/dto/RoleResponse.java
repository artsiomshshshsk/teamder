package com.github.artsiomshshshsk.findproject.dto;

public record RoleResponse (
      Long id,
      String name,
      UserResponse assignedUser,
      boolean isAvailable
){}
