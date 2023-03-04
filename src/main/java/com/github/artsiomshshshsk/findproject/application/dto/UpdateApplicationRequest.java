package com.github.artsiomshshshsk.findproject.application.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
public class UpdateApplicationRequest{
    String applicationMessage;
    String roleRequest;
    MultipartFile cv;
}
