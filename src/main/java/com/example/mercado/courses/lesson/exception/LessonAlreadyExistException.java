package com.example.mercado.courses.lesson.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class LessonAlreadyExistException extends RuntimeException {

    @Getter
    private final String code = "LESSON_ALREADY_EXIST";

    public LessonAlreadyExistException(Long moduleId, String name) {
        super(MessageFormat.format("Lesson with name: [{0}] in module: [{1}] already exists", name, moduleId));
    }
}
