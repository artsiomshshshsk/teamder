package com.github.artsiomshshshsk.findproject.user.dto;


import lombok.Builder;

@Builder
public record DashboardProjectResponse(
        Long id, String name) {
}
