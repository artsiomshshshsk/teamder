package com.github.artsiomshshshsk.findproject.user.dto;


import lombok.Builder;

@Builder
public record UserProfileResponse(
        String username,
        String resumeUrl,
        String profilePictureURL
) {
}
