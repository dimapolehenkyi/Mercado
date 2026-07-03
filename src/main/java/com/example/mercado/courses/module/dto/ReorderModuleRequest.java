package com.example.mercado.courses.module.dto;

import jakarta.validation.constraints.Positive;

public record ReorderModuleRequest(

        @Positive(
                message = "Invalid position"
        )
        Integer position

) {
}
