package com.example.mercado.users.exception.userException;

import lombok.Getter;

import java.text.MessageFormat;

public class UserAlreadyExistsException extends RuntimeException {

    @Getter
    private final String code = "USER_ALREADY_EXISTS";

    public UserAlreadyExistsException(String username, String email) {
        super(MessageFormat.format("User with username: [{0}] or email: [{1}] already exists", username, email));
    }

}
