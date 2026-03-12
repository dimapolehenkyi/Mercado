package com.example.mercado.courses.module.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleAlreadyHaveFirstPosition extends RuntimeException {

    @Getter
    private final String code = "MODULE_ALREADY_HAVE_FIRST_POSITION";

    public ModuleAlreadyHaveFirstPosition(Long moduleId) {
        super(MessageFormat.format("Module: [{0}] already have first position", moduleId));
    }
}
