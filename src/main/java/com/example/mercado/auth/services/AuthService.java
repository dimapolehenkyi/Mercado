package com.example.mercado.auth.services;

import com.example.mercado.auth.dto.AuthResponse;
import com.example.mercado.auth.dto.LoginRequest;
import com.example.mercado.common.jwt.JwtProperties;
import com.example.mercado.common.jwt.JwtService;
import com.example.mercado.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;



    public AuthResponse authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.login(),
                        loginRequest.password()
                )
        );

        String token = jwtService.generateToken(loginRequest.login(), jwtProperties.getAccessTokenExpiration());

        return new AuthResponse(token);
    }





}
