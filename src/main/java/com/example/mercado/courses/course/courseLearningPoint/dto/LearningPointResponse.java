package com.example.mercado.courses.course.courseLearningPoint.dto;

public record LearningPointResponse(

        Long id,

        Long courseId,

        String text,

        Integer position

) {
}
