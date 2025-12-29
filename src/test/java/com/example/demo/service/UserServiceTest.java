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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSignup() {
        SignUpRequest request = new SignUpRequest("testuser", "password");
        when(userRepository.existsById("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.signup(request);

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void shouldFailSignupWhenUserAlreadyExists() {
        SignUpRequest request = new SignUpRequest("testuser", "password");
        when(userRepository.existsById("testuser")).thenReturn(true);

        assertThrows(AppException.class, () -> userService.signup(request));
        verify(userRepository, never()).save(any(User.class));
    }

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

    @Test
    void shouldFailAuthenticateWhenUserNotFound() {
        when(userRepository.findById("testuser")).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> userService.authenticate("testuser", "password"));
    }

    @Test
    void shouldFailAuthenticateWhenInvalidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(AppException.class, () -> userService.authenticate("testuser", "wrongpassword"));
    }
}
