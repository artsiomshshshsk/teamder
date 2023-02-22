package com.github.artsiomshshshsk.findproject.application.dto;

import com.github.artsiomshshshsk.findproject.application.ApplicationStatus;
import com.github.artsiomshshshsk.findproject.role.dto.RoleResponse;
import com.github.artsiomshshshsk.findproject.user.dto.UserResponse;
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
