package com.example.mercado.courses.lesson.dto;

import com.example.mercado.courses.lesson.enums.LessonStatus;

public record LessonResponse(

        Long id,

        Long moduleId,

        String name,

        String description,

        LessonStatus status,

        Integer duration,

        Integer position

) {
}
