package com.example.demo.service;

import com.example.demo.dto.SignUpRequest;
import com.example.demo.exception.AppException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signup(SignUpRequest signupRequest) {
        if (userRepository.existsById(signupRequest.username())) {
            throw new AppException("OD-06");
        }
        User user = new User();
        user.setUsername(signupRequest.username());
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        userRepository.save(user);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new AppException("OD-02"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException("OD-03");
        }
        return user;
    }
}
