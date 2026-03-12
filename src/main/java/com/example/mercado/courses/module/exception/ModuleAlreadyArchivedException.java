package com.example.mercado.courses.module.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleAlreadyArchivedException extends RuntimeException {

    @Getter
    private final String code = "MODULE_ALREADY_ARCHIVED";

    public ModuleAlreadyArchivedException(Long moduleId) {
        super(MessageFormat.format("Module: [{0}]  already archived", moduleId));
    }
}
