package com.example.mercado.courses.course.courseRequirement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddRequirementRequest(

        @NotBlank
        @Size(
                max = 1000,
                message = "Text can't contains more then 1000 characters"
        )
        String text

) {
}
