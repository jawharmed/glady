package com.glady.challenge.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(Exception ex){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(apiError,apiError.getStatus());
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<Object> handleEntityAlreadyExistException(Exception ex){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();

        return new ResponseEntity<>(apiError,apiError.getStatus());
    }
}
