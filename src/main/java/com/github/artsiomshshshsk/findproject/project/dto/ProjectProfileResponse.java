package com.github.artsiomshshshsk.findproject.project.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public record ProjectProfileResponse(
        Long id,
        String name,

        String shortDescription,

        String description,

        List<ProjectProfileOpenedRoleResponse> openedRoles,

        LocalDateTime publishedAt,

        int teamSize,

        int occupiedPlaces,

        List<TeamMemberResponse> teamMembers,

        Long ownerId

) {
}
