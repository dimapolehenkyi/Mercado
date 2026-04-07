package com.example.mercado.courses.course.courseRequirement.exception;

import lombok.Getter;

public class CourseRequirementPositionBelowZeroException extends RuntimeException {

    @Getter
    private final String code = "POSITION_BELOW_ZERO";

    public CourseRequirementPositionBelowZeroException() {
        super("Position below zero");
    }
}
