package com.github.artsiomshshshsk.findproject.user.dto;

import lombok.Builder;

@Builder
public record CurrentUserProfile(
        Long id,
        String username,
        String avatarUrl
) {
}
