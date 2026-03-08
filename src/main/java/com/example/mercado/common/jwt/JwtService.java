package com.example.mercado.common.jwt;

import com.example.mercado.common.jwt.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final TokenRepository tokenRepository;


    /* ================= GENERATE ================= */
    public String generateToken(String username, long expiryTime) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateAccessToken(String username) {
        return generateToken(username, jwtProperties.getAccessTokenExpiration());
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, jwtProperties.getRefreshTokenExpiration());
    }



    /* ================= EXTRACT ================= */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /* ================= VALIDATION ================= */
    public boolean isAccessTokenValid(String token, UserDetails user) {

        final String username = extractUsername(token);

        boolean existsInDb = tokenRepository.findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return username.equals(user.getUsername())
                && !isTokenExpired(token)
                && existsInDb;
    }

    public boolean isRefreshTokenValid(String token, UserDetails user) {

        final String username = extractUsername(token);

        boolean existsInDb = tokenRepository.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return username.equals(user.getUsername())
                && !isTokenExpired(token)
                && existsInDb;
    }


    /* ================= SIGN KEY ================= */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
