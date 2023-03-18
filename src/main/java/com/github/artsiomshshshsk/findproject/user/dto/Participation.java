package com.github.artsiomshshshsk.findproject.user.dto;


import lombok.Builder;

@Builder
public record Participation(
        Long projectId,
        String projectTitle,
        String shortDescription,
        String role,

        boolean isOwner
) {
}
