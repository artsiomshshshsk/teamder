package com.github.artsiomshshshsk.findproject.project.dto;

import lombok.Builder;

@Builder
public record ProjectProfileOpenedRoleResponse(
        Long id,
        String name
) {
}
