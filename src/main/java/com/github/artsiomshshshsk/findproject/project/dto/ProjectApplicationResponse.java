package com.github.artsiomshshshsk.findproject.project.dto;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record ProjectApplicationResponse(
        Long id,
        String username,
        String contact,
        String applicantMessage,
        String role,
        String resumeURL,
        LocalDateTime applicationDate
) {
}
