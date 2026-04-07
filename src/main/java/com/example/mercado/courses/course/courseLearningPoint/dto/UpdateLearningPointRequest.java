package com.example.mercado.courses.course.courseLearningPoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateLearningPointRequest(

        @NotBlank
        @Size(max = 1000)
        String text

) {
}
