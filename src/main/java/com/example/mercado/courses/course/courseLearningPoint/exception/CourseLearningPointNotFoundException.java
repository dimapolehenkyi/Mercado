package com.example.mercado.courses.course.courseLearningPoint.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseLearningPointNotFoundException extends RuntimeException {


    @Getter
    private final String code = "COURSE_LEARNING_POINT_NOT_FOUND";

    public CourseLearningPointNotFoundException(Long courseId, Long pointId) {
        super(MessageFormat.format("Course Learning Point: [{0}] in course: [{1}] not found", pointId, courseId));
    }
}
