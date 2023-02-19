package com.github.artsiomshshshsk.findproject.dto.catalog;

import com.github.artsiomshshshsk.findproject.dto.RoleResponse;
import java.util.List;

public record CatalogProjectResponse(
        Long id,
        String name,
        String shortDescription,
        List<RoleResponse> roles
) {
}
