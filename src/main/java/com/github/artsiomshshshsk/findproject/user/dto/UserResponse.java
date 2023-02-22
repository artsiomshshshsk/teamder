package com.github.artsiomshshshsk.findproject.user.dto;

import lombok.Builder;

@Builder
public record UserResponse (
    Long id,
    String username,
    String profilePictureURL
){}
