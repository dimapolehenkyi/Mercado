package com.example.mercado.auth.service;


import com.example.mercado.auth.dto.AuthResponse;
import com.example.mercado.auth.dto.LoginRequest;
import com.example.mercado.auth.services.PasswordAuthService;
import com.example.mercado.common.jwt.JwtProperties;
import com.example.mercado.common.jwt.JwtService;
import com.example.mercado.users.exception.userException.UserAlreadyExistsException;
import com.example.mercado.users.dto.RegisterRequest;
import com.example.mercado.users.entity.User;
import com.example.mercado.users.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordAuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private PasswordAuthService authService;

    @Test
    @DisplayName("should authenticate user and return token")
    void shouldAuthenticateAndReturnToken() {

        LoginRequest loginRequest = new LoginRequest("username", "password", "password");
        String expectedToken = "mocked-jwt-token";

        when(jwtProperties.getAccessTokenExpiration()).thenReturn(3600L);
        when(jwtService.generateToken("username", 3600L))
                .thenReturn(expectedToken);

        AuthResponse authResponse = authService.authenticate(loginRequest);

        Assertions.assertNotNull(authResponse);
        Assertions.assertEquals(expectedToken, authResponse.accessToken());

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken("username", 3600L);
    }

    @Test
    @DisplayName("should throw exception when authentication fails")
    void shouldThrowExceptionWhenAuthenticationFails () {
        LoginRequest loginRequest = new LoginRequest("badUsername", "badPassword", "password");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any());

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.authenticate(loginRequest));

        verify(jwtService, never()).generateToken(any(), anyLong());
    }

    @Test
    @DisplayName("should return AuthResponse when all valid")
    void register_shouldReturnAuthResponse_whenAllValid() {
        RegisterRequest request = new RegisterRequest(
                "test",
                "taken@example.com",
                "password",
                "confirmationPassword");

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateAccessToken(request.username())).thenReturn("mocked-jwt-token");


        AuthResponse authResponse = authService.register(request);

        Assertions.assertNotNull(authResponse);
        Assertions.assertEquals("mocked-jwt-token", authResponse.accessToken());
    }

    @Test
    @DisplayName("should throw exception when email taken")
    void register_shouldThrowException_whenEmailTaken() {
        RegisterRequest request = new RegisterRequest(
                "test",
                "taken@example.com",
                "password",
                "confirmationPassword");

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("should throw exception when username taken")
    void register_shouldThrowException_whenUsernameTaken() {
        RegisterRequest request = new RegisterRequest(
                "test",
                "taken@example.com",
                "password",
                "confirmationPassword");

        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }
}
