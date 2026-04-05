package com.example.mercado.courses.course.courseRequirement.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class TooManyRequirementException extends RuntimeException {

    @Getter
    private final String code = "TOO_MANY_REQUIREMENT";

    public TooManyRequirementException(Long courseId) {
        super(MessageFormat.format("Course: [{0}] already have too many requirements", courseId));
    }
}
