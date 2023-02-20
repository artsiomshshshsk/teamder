package com.github.artsiomshshshsk.findproject.exception;

import com.github.artsiomshshshsk.findproject.dto.ExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class AppExceptionHandler {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(errors.toString())
                .timestamp(LocalDateTime.now())
                .build(),HttpStatus.BAD_REQUEST);
    }
}