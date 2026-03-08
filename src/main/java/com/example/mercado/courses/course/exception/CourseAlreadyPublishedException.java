package com.example.mercado.courses.course.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseAlreadyPublishedException extends RuntimeException {

    @Getter
    private final String code = "COURSE_ALREADY_PUBLISHED";

    public CourseAlreadyPublishedException(Long courseId) {
        super(MessageFormat.format("Course: [{0}] already published", courseId));
    }
}
