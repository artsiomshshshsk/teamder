package com.github.artsiomshshshsk.findproject.exception.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private String message;
    private LocalDateTime timestamp;
}
