package com.github.artsiomshshshsk.findproject.project.dto;

import com.github.artsiomshshshsk.findproject.role.dto.RoleRequest;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
@Builder
public record ProjectRequest(

        @NotBlank(message = "Project name is mandatory")
        @Size(max = 15, message = "Project name can be 15 characters max")
        String name,
        @NotBlank(message = "Short description is mandatory")
        @Size(max = 300, message = "Short description can be 300 characters max")
        String shortDescription,

        @NotBlank(message = "Description is mandatory")
        @Size(max = 1000, message = "Description can be 1000 characters max")
        String description,

        @NotNull(message = "Roles are mandatory")
        List<RoleRequest> roles,
        @NotBlank(message = "Chat invitation link is mandatory")
        String chatInviteLink,
        @NotNull(message = "Owner Role is mandatory")
        RoleRequest ownerRole) {
}
