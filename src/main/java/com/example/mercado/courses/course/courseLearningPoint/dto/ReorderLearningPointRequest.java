package com.example.mercado.courses.course.courseLearningPoint.dto;

import jakarta.validation.constraints.Positive;

public record ReorderLearningPointRequest(

        Long id,

        @Positive
        Integer position

) {
}
