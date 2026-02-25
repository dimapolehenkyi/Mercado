package com.example.mercado.common.jwt;

import com.example.mercado.users.services.interfaces.UserService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @Test
    void doFilterInternal_setsAuthentication_whenTokenValid() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("Authorization", "Bearer valid-token");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@gmail.com",
                "password",
                List.of(() -> "ROLE_USER")
        );

        when(jwtService.extractUsername("valid-token")).thenReturn("test@gmail.com");
        when(userService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.isAccessTokenValid("valid-token", userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_doesNotSetAuthentication_whenNoHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilterInternal(request, response, filterChain);
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
