package com.github.artsiomshshshsk.findproject.project.dto;

import com.github.artsiomshshshsk.findproject.application.ApplicationStatus;
import com.github.artsiomshshshsk.findproject.role.dto.RoleResponse;
import com.github.artsiomshshshsk.findproject.user.dto.UserResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppliedResponse(
        Long applicationId,
        String resumeURL,
        String message,
        String roleRequest,
        LocalDateTime applicationDate,
        ApplicationStatus status
) {
}
