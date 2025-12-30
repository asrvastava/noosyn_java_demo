package com.example.demo.service;

import com.example.demo.dto.SignUpRequest;
import com.example.demo.exception.AppException;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class UserService implements UserDetailsService {

    private static final String USER_ALREADY_EXISTS_CODE = "OD-06";
    private static final String USER_NOT_FOUND_CODE = "OD-02";
    private static final String INVALID_PASSWORD_CODE = "OD-03";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignUpRequest signupRequest) {
        log.info("Attempting to sign up user: {}", signupRequest.username());
        if (userRepository.existsById(signupRequest.username())) {
            log.warn("User already exists: {}", signupRequest.username());
            throw new AppException(USER_ALREADY_EXISTS_CODE);
        }
        User user = new User();
        user.setUsername(signupRequest.username());
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        userRepository.save(user);
        log.info("User registered successfully: {}", signupRequest.username());
    }

    public User authenticate(String username, String password) {
        log.debug("Authenticating user: {}", username);
        User user = userRepository.findById(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND_CODE));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Invalid password for user: {}", username);
            throw new AppException(INVALID_PASSWORD_CODE);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> authorities = roleRepository.findByUserUsername(username).stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}
