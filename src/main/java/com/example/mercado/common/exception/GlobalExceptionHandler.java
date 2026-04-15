package com.example.mercado.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;


    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(
            AppException ex,
            HttpServletRequest request,
            Locale locale
    ) {

        String message = messageSource.getMessage(
                ex.getCode().getKey(),
                ex.getArgs(),
                locale
        );

        String safePath = HtmlUtils.htmlEscape(request.getRequestURI());

        return ResponseEntity
                .status(ex.getCode().getStatus())
                .body(new ErrorResponse(
                        ex.getCode().getStatus().value(),
                        ex.getCode().name(),
                        message,
                        safePath,
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