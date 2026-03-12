package com.example.mercado.courses.module.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleNotFoundException extends RuntimeException {

    @Getter
    private final String code = "MODULE_NOT_FOUND";

    public ModuleNotFoundException(Long moduleId) {
        super(MessageFormat.format("Module: [{0}] not found", moduleId));
    }

    public ModuleNotFoundException(Long courseId, Integer position) {
        super(MessageFormat.format("Module in a course: [{0}] and position: [{1}] not found", courseId, position));
    }
}
