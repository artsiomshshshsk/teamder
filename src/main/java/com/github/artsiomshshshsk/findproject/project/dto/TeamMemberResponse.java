package com.github.artsiomshshshsk.findproject.project.dto;

import lombok.Builder;

@Builder
public record TeamMemberResponse(

        Long userId,
        String profilePictureURL,
        String username,
        String roleName
) {
}
