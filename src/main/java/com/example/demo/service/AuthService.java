package com.example.demo.service;

import com.example.demo.util.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication and token operations.
 * Manages user authentication and JWT token generation/validation.
 */
@Service
@lombok.RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the generated JWT token
     */
    public String authenticateAndGenerateToken(String username, String password) {
        log.info("Authenticating user: {}", username);
        userService.authenticate(username, password);
        String token = jwtUtil.generateToken(username);
        log.info("Token generated successfully for user: {}", username);
        return token;
    }

    /**
     * Validates a JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        log.debug("Validating token");
        return jwtUtil.validateToken(token);
    }
}
