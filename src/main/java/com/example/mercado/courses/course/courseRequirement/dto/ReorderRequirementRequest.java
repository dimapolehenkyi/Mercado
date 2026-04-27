package com.example.mercado.courses.course.courseRequirement.dto;

import jakarta.validation.constraints.Positive;

public record ReorderRequirementRequest(

        @Positive(
                message = "Invalid position"
        )
        Integer position

) {
}
