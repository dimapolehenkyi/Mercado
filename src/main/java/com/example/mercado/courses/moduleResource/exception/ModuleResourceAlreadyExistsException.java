package com.example.mercado.courses.moduleResource.exception;

import lombok.Getter;

public class ModuleResourceAlreadyExistsException extends RuntimeException {

    @Getter
    private final String code = "MODULE_RESOURCE_ALREADY_EXISTS";

    public ModuleResourceAlreadyExistsException() {
        super("Module Resource Already Exists");
    }
}
