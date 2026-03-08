package com.example.mercado.users.exception.userException;

import lombok.Getter;

import java.text.MessageFormat;

public class UserInactiveException extends RuntimeException {

    @Getter
    private final String code = "USER_INACTIVE";

    public UserInactiveException(Long id) {
        super(MessageFormat.format("User: [{0}] is inactive.", id));
    }

}
