package com.github.artsiomshshshsk.findproject.dto;

import org.springframework.web.multipart.MultipartFile;

public record ApplicationRequest(
        String applicationMessage,
        String roleRequest,
        MultipartFile cv
) {
}
