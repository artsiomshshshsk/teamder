package com.github.artsiomshshshsk.findproject.advice;

import com.github.artsiomshshshsk.findproject.exception.*;
import com.github.artsiomshshshsk.findproject.exception.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEmailException(Exception ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ExceptionResponse response = ExceptionResponse.builder()
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEmailException(DuplicateResourceException ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ApplicationCreationException.class,ApplicationDecisionException.class})
    public ResponseEntity<ExceptionResponse> handleApplicationCreationException(Exception ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.FORBIDDEN);
    }


    private ExceptionResponse getExceptionResponse(Exception exception){
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}