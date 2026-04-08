package com.example.mercado.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(
            AppException ex
    ) {
        return ResponseEntity
                .status(ex.getCode().getCode())
                .body(new ErrorResponse(
                        ex.getCode().getCode().value(),
                        ex.getCode().name(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCredentials(
            BadCredentialsException ex
    ) {
        return ex.getMessage();
    }

}