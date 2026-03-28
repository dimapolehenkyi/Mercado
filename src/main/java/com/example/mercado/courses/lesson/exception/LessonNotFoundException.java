package com.example.mercado.courses.lesson.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class LessonNotFoundException extends RuntimeException {

    @Getter
    private final String code = "LESSON_NOT_FOUND";

    public LessonNotFoundException(Long lessonId, Long moduleId) {
        super(MessageFormat.format("Lesson: [{0}] in module: [{1}] not found", lessonId,  moduleId));
    }
}
