package com.example.mercado.courses.course.courseLearningPoint.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseLearningPointAlreadyHaveSameTextException extends RuntimeException {

    @Getter
    private final String code = "COURSE_LEARNING_POINT_ALREADY_HAVE_SAME_TEXT";

    public CourseLearningPointAlreadyHaveSameTextException(Long id) {
        super(MessageFormat.format("Course Learning Point: [{0}] already have same text", id));
    }
}
