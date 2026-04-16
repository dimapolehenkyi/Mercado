package com.example.mercado.courses.course.courseRequirement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRequirementRequest(

        @NotBlank
        @Size(max = 1000)
        String text

) {
}
