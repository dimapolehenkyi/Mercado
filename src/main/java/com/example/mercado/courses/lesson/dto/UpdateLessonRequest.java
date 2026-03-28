package com.example.mercado.courses.lesson.dto;

import com.example.mercado.courses.lesson.enums.LessonStatus;

public record UpdateLessonRequest(

        String name,

        String description,

        Integer duration,

        LessonStatus status

) {
}
