package com.example.mercado.exception;


import com.example.mercado.exception.dto.ErrorResponse;
import com.example.mercado.exception.exceptions.mailException.EmailSendFailedException;
import com.example.mercado.exception.exceptions.userException.InvalidUserRoleException;
import com.example.mercado.exception.exceptions.userException.UserAlreadyExistsException;
import com.example.mercado.exception.exceptions.userException.UserInactiveException;
import com.example.mercado.exception.exceptions.userException.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFount(@NotNull UserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(@NotNull UserAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<ErrorResponse> handleUserInactive(@NotNull UserInactiveException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserRole(@NotNull InvalidUserRoleException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<ErrorResponse> emailSendFailed(@NotNull EmailSendFailedException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return  ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
