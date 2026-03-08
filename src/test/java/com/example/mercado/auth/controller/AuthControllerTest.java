package com.example.mercado.auth.controller;

import com.example.mercado.auth.dto.AuthResponse;
import com.example.mercado.auth.services.PasswordAuthService;
import com.example.mercado.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PasswordAuthService authService;

    @Test
    @DisplayName("Should return token when login is valid")
    void shouldReturnTokenWhenLoginIsValid () throws Exception {

        String token = "mocked-jwt-token";

        when(authService.authenticate(any())).thenReturn(new AuthResponse(token));

        String json = """
                {
                    "login": "username",
                    "password": "password"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(token));

    }

    @Test
    @DisplayName("Should return exception Unauthorized when auth fails")
    void shouldReturnUnauthorizedWhenAuthFails () throws Exception {

        when(authService.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        String json = """
                {
                    "login": "badUsername",
                    "password": "badPassword"
                }
                """;

        mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isUnauthorized());


    }

}
