package com.github.artsiomshshshsk.findproject.dto;

import lombok.Builder;

@Builder
public record UserResponse (
    Long id,
    String username,
    String profilePictureURL
){}
