package com.example.mercado.courses.course.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseAlreadyArchivedException extends RuntimeException {

    @Getter
    private final String code = "COURSE_ALREADY_ARCHIVED";

    public CourseAlreadyArchivedException(Long courseId) {
        super(MessageFormat.format("Course: [{0}] already archived", courseId));
    }
}
