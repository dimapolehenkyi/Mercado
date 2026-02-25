package com.example.mercado.exception.exceptions.userException;

import com.example.mercado.users.enums.Role;
import lombok.Getter;

import java.text.MessageFormat;

public class InvalidUserRoleException extends RuntimeException{

    @Getter
    private final String code = "INVALID_USER_ROLE";

    public InvalidUserRoleException(Role role){
        super(MessageFormat.format("Invalid role provided: {0}", role));
    }

}
