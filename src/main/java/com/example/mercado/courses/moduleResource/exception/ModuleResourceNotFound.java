package com.example.mercado.courses.moduleResource.exception;

import lombok.Getter;

import java.text.MessageFormat;

public class ModuleResourceNotFound extends RuntimeException {

    @Getter
    private final String code = "MODULE_RESOURCE_NOT_FOUND";

    public ModuleResourceNotFound(Long moduleResourceId) {
        super(MessageFormat.format("Module resource: [{0}] not found", moduleResourceId));
    }
}
