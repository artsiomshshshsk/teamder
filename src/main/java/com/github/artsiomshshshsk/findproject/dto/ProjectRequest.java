package com.github.artsiomshshshsk.findproject.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record ProjectRequest(
        String name,
        String shortDescription,
        String description,
        List<RoleRequest> roles,
        String chatInviteLink,
        RoleRequest ownerRole) {
}
