package com.example.mercado.courses.module.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReorderModuleRequest(

        @Positive(
                message = "Invalid position"
        )
        @NotNull
        Integer position

) {
}
