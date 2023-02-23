package com.github.artsiomshshshsk.findproject.exception;

import com.github.artsiomshshshsk.findproject.exception.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEmailException(DuplicateEmailException ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ApplicationCreationException.class, ApplicationDecisionException.class})
    public ResponseEntity<ExceptionResponse> handleApplicationCreationException(ApplicationCreationException ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(getExceptionResponse(ex), HttpStatus.NOT_FOUND);
    }


    private ExceptionResponse getExceptionResponse(Exception exception){
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}