package com.example.mercado.courses.lessonContent.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class LessonContentAlreadyExists extends RuntimeException {

    @Getter
    private final String code = "LESSON_CONTENT_ALREADY_EXISTS";

    public LessonContentAlreadyExists(String name, Long lessonId) {
        super(MessageFormat.format("Lesson content with name: [{0}], and in lessonId: [{1}] already exists", name, lessonId));
    }
}
