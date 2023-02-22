package com.github.artsiomshshshsk.findproject.project.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record CatalogProjectResponse(
        Long id,
        String name,
        String shortDescription,
        int teamSize,
        int occupiedPlaces,
        List<String> openedRoles,
        List<String> avatarURLs
) {
}
