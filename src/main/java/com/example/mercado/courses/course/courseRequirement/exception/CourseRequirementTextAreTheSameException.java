package com.example.mercado.courses.course.courseRequirement.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseRequirementTextAreTheSameException extends RuntimeException {

    @Getter
    private final String code = "TEXT_ARE_THE_SAME";

    public CourseRequirementTextAreTheSameException(Long courseId, Long courseRequirementId) {
        super(MessageFormat.format("Already same text in requirement: [{0}] in course: [{1}]", courseRequirementId, courseId));
    }

    public CourseRequirementTextAreTheSameException() {
        super("Text should be different!");
    }
}
