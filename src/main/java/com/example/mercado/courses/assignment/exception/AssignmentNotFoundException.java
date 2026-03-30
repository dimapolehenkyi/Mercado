package com.example.mercado.courses.assignment.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class AssignmentNotFoundException extends RuntimeException {

    @Getter
    private final String code = "ASSIGNMENT_NOT_FOUND";

    public AssignmentNotFoundException(Long lessonId, Long id) {
        super(MessageFormat.format("Assignment: [{0}] in lesson: [{1}] not found", id, lessonId));
    }
}
