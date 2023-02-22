package com.github.artsiomshshshsk.findproject.role.dto;

import lombok.Builder;
import javax.validation.constraints.NotBlank;

@Builder
public record RoleRequest(

        @NotBlank(message = "Role name is mandatory")
        String name
) {
}
