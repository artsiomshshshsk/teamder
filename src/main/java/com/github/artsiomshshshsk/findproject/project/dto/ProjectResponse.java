package com.github.artsiomshshshsk.findproject.project.dto;


import com.github.artsiomshshshsk.findproject.role.dto.RoleResponse;
import com.github.artsiomshshshsk.findproject.user.dto.UserResponse;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProjectResponse (
     Long id,
     String name,
     String shortDescription,
     String description,
     List<RoleResponse> roles,
     UserResponse owner,
     LocalDateTime publishedAt
){}
