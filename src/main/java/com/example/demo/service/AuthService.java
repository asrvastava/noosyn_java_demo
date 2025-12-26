package com.example.demo.service;

import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticateAndGenerateToken(String username, String password) {
        userService.authenticate(username, password);
        return jwtUtil.generateToken(username);
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
