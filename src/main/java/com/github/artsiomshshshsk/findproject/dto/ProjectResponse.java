package com.github.artsiomshshshsk.findproject.dto;


import com.github.artsiomshshshsk.findproject.domain.ProjectStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse (
     Long id,
     String name,
     String shortDescription,
     String description,
     List<RoleResponse> roles,
     UserResponse owner,
     LocalDateTime publishedAt,
     ProjectStatus status
){}
