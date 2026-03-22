package com.example.mercado.courses.lessonContent.dto;

import com.example.mercado.courses.lessonContent.enums.LessonContentType;

public record LessonContentResponse(

        Long id,
        Long lessonId,
        String name,
        String description,
        Integer position,
        String url,
        String thumbnailUrl,
        LessonContentType type

) {
}
