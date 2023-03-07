package com.github.artsiomshshshsk.findproject.user.dto;

import com.github.artsiomshshshsk.findproject.application.ApplicationStatus;
import lombok.Builder;
import java.time.LocalDateTime;


@Builder
public record DashboardApplicationResponse(
        Long id,
        String projectName,
        String applicantMessage,
        String role,
        LocalDateTime applicationDate,
        ApplicationStatus status

) {
}
