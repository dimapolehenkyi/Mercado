package com.example.mercado.courses.course.courseRequirement.dto;

public record RequirementResponse(

        Long id,

        Long courseId,

        String text,

        Integer position

) {
}
