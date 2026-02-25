package com.example.mercado.auth.controller;

import com.example.mercado.auth.dto.AuthResponse;
import com.example.mercado.auth.dto.LoginRequest;
import com.example.mercado.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

}
