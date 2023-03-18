package com.github.artsiomshshshsk.findproject.user.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileResponse(

        Long id,
        String username,
        String profileSummary,
        String avatarUrl,
        List<Participation> participations
) {
}
