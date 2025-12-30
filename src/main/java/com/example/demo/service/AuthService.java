package com.example.demo.service;

import com.example.demo.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@lombok.RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public String authenticateAndGenerateToken(String username, String password) {
        log.info("Authenticating user: {}", username);
        userService.authenticate(username, password);
        String token = jwtUtil.generateToken(username);
        log.info("Token generated successfully for user: {}", username);
        return token;
    }

    public boolean validateToken(String token) {
        log.debug("Validating token");
        return jwtUtil.validateToken(token);
    }
}
