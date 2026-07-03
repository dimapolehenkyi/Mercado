package com.example.mercado.courses.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateModuleRequest(

        @NotBlank
        String name,

        @Size(min = 1, max = 1000)
        String description

) {
}
