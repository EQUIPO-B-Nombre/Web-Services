package com.oncontigo.api;

import com.oncontigo.api.iam.infrastructure.tokens.jwt.services.TokenServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    private TokenServiceImpl tokenService;
    private Key signingKey;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl();
        tokenService.secret = "4f1feeca525de4cdb064656007da3edac7895a87ff0ea865693300fb8b6e8f9c";
        tokenService.expirationDays = 31;
        signingKey = Keys.hmacShaKeyFor(tokenService.secret.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = Jwts.builder()
                .setSubject("usuario_prueba")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(signingKey)
                .compact();

        assertTrue(tokenService.validateToken(token));
    }

    @Test
    void testValidateToken_ExpiredToken() {
        String token = Jwts.builder()
                .setSubject("usuario_prueba")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expirado
                .signWith(signingKey)
                .compact();

        assertFalse(tokenService.validateToken(token));
    }

    @Test
    void testValidateToken_MalformedToken() {
        String malformedToken = "token_invalido";

        assertFalse(tokenService.validateToken(malformedToken));
    }
}