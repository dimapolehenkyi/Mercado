package com.example.mercado.common.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode code;

    public AppException(ErrorCode code) {
        super(code.name());
        this.code = code;
    }

}
