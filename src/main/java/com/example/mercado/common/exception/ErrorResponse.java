package com.example.mercado.common.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ErrorResponse(

        @NotNull
        int status,

        @NotBlank
        String error,

        @NotNull
        LocalDateTime timestamp

) {
}
