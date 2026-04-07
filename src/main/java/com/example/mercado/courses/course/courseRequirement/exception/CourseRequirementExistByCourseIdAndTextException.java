package com.example.mercado.courses.course.courseRequirement.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class CourseRequirementExistByCourseIdAndTextException extends RuntimeException {

    @Getter
    private final String code = "EXIST_BY_COURSE_ID_AND_TEXT";

    public CourseRequirementExistByCourseIdAndTextException(Long courseId) {
        super(MessageFormat.format("Requirement in course: [{0}] already exists", courseId));
    }
}
