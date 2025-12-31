package com.example.demo.service;

import com.example.demo.dto.SignUpRequest;
import com.example.demo.exception.AppException;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for user management and authentication.
 * Handles user registration, authentication, and loading user details.
 */
@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user.
     *
     * @param request the request object containing user signup details
     * @throws AppException if the user already exists
     */
    public void signup(SignUpRequest request) {
        log.info("Attempting to sign up user: {}", request.username());
        if (userRepository.existsById(request.username())) {
            log.warn("User already exists: {}", request.username());
            throw new AppException("OD-06");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        userRepository.save(user);
        log.info("User registered successfully: {}", request.username());
    }

    /**
     * Authenticates a user by checking credentials.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the authenticated user
     * @throws AppException if the user is not found or the password is invalid
     */
    public User authenticate(String username, String password) {
        log.debug("Authenticating user: {}", username);
        User user = userRepository.findById(username)
                .orElseThrow(() -> new AppException("OD-02"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Invalid password for user: {}", username);
            throw new AppException("OD-03");
        }
        return user;
    }

    /**
     * Loads user details by username for Spring Security.
     *
     * @param username the username of the user
     * @return the user details
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException if
     *                                                                                 the
     *                                                                                 user
     *                                                                                 is
     *                                                                                 not
     *                                                                                 found
     */
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new AppException("OD-02"));

        java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = roleRepository
                .findByUserUsername(username).stream()
                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getRoleName()))
                .collect(java.util.stream.Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}
