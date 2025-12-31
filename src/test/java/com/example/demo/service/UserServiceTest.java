package com.example.demo.service;

import com.example.demo.dto.SignUpRequest;
import com.example.demo.exception.AppException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService.
 * Verifies business logic for user management and authentication.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    /**
     * Test case for user signup.
     */
    @Test
    void shouldSignup() {
        SignUpRequest request = new SignUpRequest("testuser", "password");
        when(userRepository.existsById("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.signup(request);

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    /**
     * Test case for failing to signup existing user.
     */
    @Test
    void shouldFailSignupWhenUserAlreadyExists() {
        SignUpRequest request = new SignUpRequest("testuser", "password");
        when(userRepository.existsById("testuser")).thenReturn(true);

        AppException ex = assertThrows(AppException.class, () -> userService.signup(request));
        assertEquals("OD-06", ex.getErrorCode());

        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Test case for user authentication.
     */
    @Test
    void shouldAuthenticate() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User result = userService.authenticate("testuser", "password");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    /**
     * Test case for failing authentication when user is not found.
     */
    @Test
    void shouldFailAuthenticateWhenUserNotFound() {
        when(userRepository.findById("testuser")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> userService.authenticate("testuser", "password"));
        assertEquals("OD-02", ex.getErrorCode());
    }

    /**
     * Test case for failing authentication with invalid credentials.
     */
    @Test
    void shouldFailAuthenticateWhenInvalidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> userService.authenticate("testuser", "wrongpassword"));
        assertEquals("OD-03", ex.getErrorCode());
    }
}
