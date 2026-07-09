package com.example.mercado.courses.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateModuleRequest(

        @NotBlank(
                message = "Name can't be blank"
        )
        @Pattern(
                regexp = "^[a-zA-Z0-9]+(\\s[a-zA-Z0-9]+)*$",
                message = "Invalid keyword"
        )
        String name,

        @Size(
                max = 2000,
                message = "Description too long"
        )
        String description

) {
}
