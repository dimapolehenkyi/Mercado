package com.example.mercado.courses.module.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleAlreadyPublishedException extends RuntimeException {

    @Getter
    private final String code = "MODULE_ALREADY_PUBLISHED";

    public ModuleAlreadyPublishedException(Long moduleId) {
        super(MessageFormat.format("Module: [{0}]  already published", moduleId));
    }
}
