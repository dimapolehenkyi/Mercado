package com.example.mercado.courses.course.courseLearningPoint.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseLearningPointAlreadyExistException extends RuntimeException {

    @Getter
    private final String code = "COURSE_LEARNING_POINT_ALREADY_EXIST";

    public CourseLearningPointAlreadyExistException(Long courseId) {
        super(MessageFormat.format("Course Learning Point in course: [{0}] already exists", courseId));
    }
}
