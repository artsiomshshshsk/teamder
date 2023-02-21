package com.github.artsiomshshshsk.findproject.dto;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;

public record ApplicationRequest(
        @NotBlank
        String applicationMessage,

        @NotBlank
        String roleRequest,

        MultipartFile cv
) {
}
