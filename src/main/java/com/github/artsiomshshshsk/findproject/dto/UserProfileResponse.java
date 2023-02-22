package com.github.artsiomshshshsk.findproject.dto;


import lombok.Builder;

@Builder
public record UserProfileResponse(
        String username,
        String resumeUrl,
        String profilePictureURL
) {
}
