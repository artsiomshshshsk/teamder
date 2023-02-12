package com.github.artsiomshshshsk.findproject.dto;

import com.github.artsiomshshshsk.findproject.domain.ProjectStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
@Builder
public record ProjectRequest(
        String name,
        String shortDescription,
        String description,
        List<RoleRequest> roles,
        RoleRequest ownerRole) {
}
