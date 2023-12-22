package com.glady.challenge.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(Exception ex){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError,apiError.getStatus());
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<Object> handleEntityAlreadyExistException(Exception ex){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError,apiError.getStatus());
    }

    @ExceptionHandler(GladyException.class)
    public ResponseEntity<Object> handleGladyException(Exception ex){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();

        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError,apiError.getStatus());
    }
}
