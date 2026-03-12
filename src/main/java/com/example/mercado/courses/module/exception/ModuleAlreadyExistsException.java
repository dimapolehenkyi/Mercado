package com.example.mercado.courses.module.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleAlreadyExistsException extends RuntimeException {

    @Getter
    private final String code = "MODULE_ALREADY_EXISTS";

    public ModuleAlreadyExistsException(String name) {
        super(MessageFormat.format("Module: [{0}] already exists", name));
    }
}
