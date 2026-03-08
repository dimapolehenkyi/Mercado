package com.example.mercado.courses.course.exception;

import lombok.Getter;

public class CourseAlreadyExistsException extends RuntimeException {

    @Getter
    private final String code = "COURSE_ALREADY_EXISTS";

    public CourseAlreadyExistsException(String name) {
        super(String.format("Course with name: [ %s ] already exists", name));
    }
}
