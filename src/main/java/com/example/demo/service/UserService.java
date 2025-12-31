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

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
