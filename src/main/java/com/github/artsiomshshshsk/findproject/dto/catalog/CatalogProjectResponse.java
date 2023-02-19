package com.github.artsiomshshshsk.findproject.dto.catalog;

import com.github.artsiomshshshsk.findproject.dto.RoleResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record CatalogProjectResponse(
        Long id,
        String name,
        String shortDescription,
        List<RoleResponse> roles
) {
}
