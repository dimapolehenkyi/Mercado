package com.example.mercado.exception.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ErrorResponse(

        @NotNull
        int status,

        @NotBlank
        String error,

        @NotBlank
        String message,

        @NotNull
        LocalDateTime timestamp

) {
}
