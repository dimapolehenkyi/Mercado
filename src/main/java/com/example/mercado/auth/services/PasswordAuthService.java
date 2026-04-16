package com.example.mercado.auth.services;

import com.example.mercado.auth.dto.AuthResponse;
import com.example.mercado.auth.dto.LoginRequest;
import com.example.mercado.auth.services.interfaces.AuthServiceImpl;
import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.common.jwt.JwtProperties;
import com.example.mercado.common.jwt.JwtService;
import com.example.mercado.users.dto.RegisterRequest;
import com.example.mercado.users.mappers.UserMapper;
import com.example.mercado.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("PasswordAuthService")
@RequiredArgsConstructor
@Transactional
public class PasswordAuthService implements AuthServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
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

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        var user = UserMapper.toEntity(registerRequest, passwordEncoder);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(
                    ErrorCode.USER_ALREADY_EXISTS,
                    user.getId()
            );
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(
                    ErrorCode.USER_ALREADY_EXISTS,
                    user.getId()
            );
        }

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getUsername());

        return new AuthResponse(accessToken);
    }
}
