package com.example.mercado.common.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode code;
    private final Object[] args;

    public AppException(ErrorCode code, Object... args) {
        this.code = code;
        this.args = args;
    }

}
