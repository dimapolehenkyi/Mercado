package com.example.mercado.courses.lessonContent.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class LessonContentNotFoundException extends RuntimeException {

    @Getter
    private final String code = "LESSON_CONTENT_NOT_FOUND";

    public LessonContentNotFoundException(Long lessonContentId, Long lessonId) {
        super(MessageFormat.format("LessonContent: [{0}] in Lesson: [{1}] not found", lessonContentId, lessonId));
    }
}
