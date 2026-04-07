package com.example.mercado.courses.course.courseLearningPoint.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseLearningPointPositionBelowZeroException extends RuntimeException {

    @Getter
    private final String code = "COURSE_LEARNING_POINT_POSITION_BELOW_ZERO";

    public CourseLearningPointPositionBelowZeroException(Long id) {
        super(MessageFormat.format("Course Learning Point: [{0}] position below zero", id));
    }
}
