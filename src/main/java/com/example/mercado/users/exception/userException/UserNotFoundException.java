package com.example.mercado.users.exception.userException;

import lombok.Getter;

import java.text.MessageFormat;

public class UserNotFoundException extends RuntimeException {

    @Getter
    private final String code = "USER_NOT_FOUND";

    public UserNotFoundException(Long id) {
        super(MessageFormat.format("User not found with id: [{0}]", id));
    }

    public UserNotFoundException(String email) {
        super(MessageFormat.format("User: [{0}] not found", email));
    }

    public UserNotFoundException(String username, String email) {
        super(MessageFormat.format("User not found with username: [{0}] or email: [{1}]", username, email));
    }
}
