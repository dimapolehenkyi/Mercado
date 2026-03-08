package com.example.mercado.users.mappers;

import com.example.mercado.users.dto.RegisterRequest;
import com.example.mercado.users.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserMapper {

    public static User toEntity(RegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();
    }

}
