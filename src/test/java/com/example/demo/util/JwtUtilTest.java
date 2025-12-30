package com.example.demo.util;

import com.example.demo.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtUtil jwtUtil;

    private static final String SECRET_PLAIN = "super_secret_key_for_testing_purposes_only_12345";
    private String secretEncoded;
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    @BeforeEach
    void setUp() {
        secretEncoded = Base64.getEncoder().encodeToString(SECRET_PLAIN.getBytes());
        when(jwtConfig.getSecret()).thenReturn(secretEncoded);
        jwtUtil.init();
    }

    @Test
    void shouldGenerateValidToken() {
        when(jwtConfig.getExpiration()).thenReturn(EXPIRATION);
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void shouldExtractUsernameFromToken() {
        when(jwtConfig.getExpiration()).thenReturn(EXPIRATION);
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        when(jwtConfig.getExpiration()).thenReturn(EXPIRATION);
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void shouldFailValidationForInvalidToken() {
        String invalidToken = "invalid.token.string";
        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }
}
