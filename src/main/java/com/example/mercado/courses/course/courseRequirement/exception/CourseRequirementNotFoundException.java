package com.example.mercado.courses.course.courseRequirement.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseRequirementNotFoundException extends RuntimeException {

    @Getter
    private final String code = "COURSE_REQUIREMENT_NOT_FOUND";

    public CourseRequirementNotFoundException(Long courseId, Long courseRequirementId) {
        super(MessageFormat.format("Requirement: [{0}] in course: [{1}] not found", courseRequirementId, courseId));
    }
}
