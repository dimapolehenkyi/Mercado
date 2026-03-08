package com.example.mercado.courses.course.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseNotFound extends RuntimeException {

    @Getter
    private final String code = "COURSE_NOT_FOUND";

    public CourseNotFound(Long id) {
        super(MessageFormat.format("Course: [{0}] not found", id));
    }
}
