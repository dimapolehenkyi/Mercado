package com.example.mercado.users.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9._]{3,24}$",
                message =  "Username must be 3-20 characters and contain only letters, numbers, dot or underscore"
        )
        String username,

        @Email
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Positive
        @Pattern(
                regexp = "^(?![._])(?=.*[A-Za-z])(?=.*\\d).{8,32}$",
                message = "Password must be 8-32 characters, contain letters and numbers, and not start with '.' or '_'"
        )
        String password,

        @NotBlank(message = "Confirmation password is required")
        String confirmationPassword


) {
}
