package com.github.artsiomshshshsk.findproject.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateRequest(
        String username,
        String email,
        String password,
        MultipartFile profilePicture,
        MultipartFile resume
) {
}
