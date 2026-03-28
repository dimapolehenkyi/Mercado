package com.example.mercado.courses.lesson.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class LessonStatusAreSameException extends RuntimeException {

    @Getter
    private final String code = "LESSON_STATUS_ARE_SAME";

    public LessonStatusAreSameException(Long lessonId) {
        super(MessageFormat.format("Lesson: [{0}] status are same", lessonId));
    }
}
