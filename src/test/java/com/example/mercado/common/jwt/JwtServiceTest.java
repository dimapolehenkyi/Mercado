package com.example.mercado.common.jwt;

import com.example.mercado.common.jwt.repositories.TokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Date;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private TokenRepository tokenRepository;


    @InjectMocks
    private JwtService jwtService;

    @Test
    @DisplayName("generateAccessToken should create valid token")
    void generateAccessToken_createsValidToken() {
        String email = "test@gmail.com";

        lenient().when(jwtProperties.getSecretKey()).thenReturn(
                Base64.getEncoder().encodeToString("mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey".getBytes())
        );
        lenient().when(jwtProperties.getAccessTokenExpiration()).thenReturn(3600000L);
        lenient().when(jwtProperties.getRefreshTokenExpiration()).thenReturn(7200000L);

        String token = jwtService.generateAccessToken(email);

        String extractedEmail = jwtService.extractUsername(token);
        Assertions.assertNotNull(extractedEmail);

        Assertions.assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("generateRefreshToken should create valid token")
    void generateRefreshToken_createsValidToken() {
        String email = "test@gmail.com";

        lenient().when(jwtProperties.getSecretKey()).thenReturn(
                Base64.getEncoder().encodeToString("mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey".getBytes())
        );
        lenient().when(jwtProperties.getAccessTokenExpiration()).thenReturn(3600000L);
        lenient().when(jwtProperties.getRefreshTokenExpiration()).thenReturn(7200000L);

        String token = jwtService.generateRefreshToken(email);

        String extractedEmail = jwtService.extractUsername(token);
        Assertions.assertNotNull(extractedEmail);

        Assertions.assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("extractUsername should return valid username")
    void extractUsername_shouldReturnValidUsername() {
        lenient().when(jwtProperties.getAccessTokenExpiration()).thenReturn(3600000L);
        lenient().when(jwtProperties.getRefreshTokenExpiration()).thenReturn(7200000L);
        lenient().when(jwtProperties.getSecretKey()).thenReturn(
                Base64.getEncoder().encodeToString("mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey".getBytes())
        );

        String email = "test@gmail.com";
        String token = jwtService.generateAccessToken(email);

        String extractUsername = jwtService.extractUsername(token);
        Assertions.assertNotNull(extractUsername);
        Assertions.assertEquals(email, extractUsername);

        Date expirationDate = jwtService.extractExpiration(token);
        Assertions.assertNotNull(expirationDate);
        Assertions.assertTrue(expirationDate.after(new Date()));

        Assertions.assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    @DisplayName("extractUsername should throw exception if had invalid token")
    void extractUsername_invalidToken_throwsException() {
        String invalidToken = "invalid_token";
        Assertions.assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

}
