package com.example.mercado.courses.module.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleAlreadyHaveLastPositionException extends RuntimeException {

    @Getter
    private final String code = "MODULE_ALREADY_HAVE_LAST_POSITION";

    public ModuleAlreadyHaveLastPositionException(Long moduleId) {
        super(MessageFormat.format("Module: [{0}] already have last position", moduleId));
    }
}
