package com.github.artsiomshshshsk.findproject.dto;

import com.github.artsiomshshshsk.findproject.domain.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApplicationResponse(
        Long id,
        UserResponse applicant,
        String resumeURL,
        String message,
        RoleResponse roleRequest,
        LocalDateTime applicationDate,
        ApplicationStatus status
        ) {
}
