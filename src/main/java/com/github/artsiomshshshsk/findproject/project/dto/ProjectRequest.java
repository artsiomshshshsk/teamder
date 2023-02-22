package com.github.artsiomshshshsk.findproject.project.dto;

import com.github.artsiomshshshsk.findproject.role.dto.RoleRequest;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@Builder
public record ProjectRequest(

        @NotBlank(message = "Project name is mandatory")
        String name,
        @NotBlank(message = "Short description is mandatory")
        String shortDescription,

        @NotBlank(message = "Description is mandatory")
        String description,

        @NotNull(message = "Roles are mandatory")
        List<RoleRequest> roles,
        @NotBlank(message = "Chat invitation link is mandatory")
        String chatInviteLink,
        @NotNull(message = "Owner Role is mandatory")
        RoleRequest ownerRole) {
}
