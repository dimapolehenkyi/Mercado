package com.example.mercado.courses.course.courseRequirement.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class TextAreTheSameException extends RuntimeException {

    @Getter
    private final String code = "TEXT_ARE_THE_SAME";

    public TextAreTheSameException(Long courseId, Long courseRequirementId) {
        super(MessageFormat.format("Already same text in requirement: [{0}] in course: [{1}]", courseRequirementId, courseId));
    }

    public TextAreTheSameException() {
        super("Text should be different!");
    }
}
