package com.example.mercado.courses.lesson.dto;

import com.example.mercado.courses.lesson.enums.LessonStatus;

public record LessonShortResponse(

        Long id,

        Long moduleId,

        String name,

        LessonStatus status

) {
}
