package com.example.mercado.courses.module.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleAlreadyDeletedException extends RuntimeException {

    @Getter
    private final String code = "MODULE_ALREADY_DELETED";

    public ModuleAlreadyDeletedException(Long moduleId) {
        super(MessageFormat.format("Module: [{0}] already deleted", moduleId));
    }
}
