package com.github.artsiomshshshsk.findproject.user.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateRequest(
        String username,
        String email,
        String password,
        MultipartFile profilePicture,
        MultipartFile resume
) {
}
