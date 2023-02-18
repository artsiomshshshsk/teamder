package com.github.artsiomshshshsk.findproject.dto;

import lombok.Builder;

@Builder
public record UserResponse (
    String username,
    Long id,

    String profilePictureURL
){}
