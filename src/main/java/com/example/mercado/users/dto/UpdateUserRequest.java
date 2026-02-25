package com.example.mercado.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record UpdateUserRequest(

        @Pattern(
                regexp = "^[a-zA-Z0-9._]{3,24}$",
                message =  "Username must be 3-20 characters and contain only letters, numbers, dot or underscore"
        )
        String username,

        @Email
        String email,

        @Positive
        @Pattern(
                regexp = "^(?![._])(?=.*[A-Za-z])(?=.*\\d).{8,32}$",
                message = "Password must be 8-32 characters, contain letters and numbers, and not start with '.' or '_'"
        )
        String password
) {
}
