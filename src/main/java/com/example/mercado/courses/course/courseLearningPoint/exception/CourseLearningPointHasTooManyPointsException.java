package com.example.mercado.courses.course.courseLearningPoint.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseLearningPointHasTooManyPointsException extends RuntimeException {

    @Getter
    private final String code = "COURSE_LEARNING_POINT_HAS_TOO_MANY_POINTS";

    public CourseLearningPointHasTooManyPointsException(Long courseId) {
        super(MessageFormat.format("In course: [{0}] point's limit are reached", courseId));
    }
}
