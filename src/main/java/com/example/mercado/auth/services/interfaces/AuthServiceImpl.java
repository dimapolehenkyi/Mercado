package com.example.mercado.auth.services.interfaces;

import com.example.mercado.auth.dto.AuthResponse;
import com.example.mercado.auth.dto.LoginRequest;
import com.example.mercado.users.dto.RegisterRequest;

public interface AuthServiceImpl {

    AuthResponse authenticate(LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest);
}
