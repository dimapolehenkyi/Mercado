package com.example.mercado.courses.lessonContent.dto;

import com.example.mercado.courses.lessonContent.enums.LessonContentType;

public record UpdateLessonContentRequest(

        String name,
        String description,
        LessonContentType type,
        String url,
        String thumbnailUrl

) {
}
