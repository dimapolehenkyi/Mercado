package com.example.mercado.courses.assignment.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class AssignmentAlreadyExistException extends RuntimeException {

    @Getter
    private final String code = "ASSIGNMENT_ALREADY_EXIST";

    public AssignmentAlreadyExistException(Long lessonId, String name) {
        super(MessageFormat.format("Assignment: [{0}] in lesson: [{1}] already exists", name, lessonId));
    }
}
