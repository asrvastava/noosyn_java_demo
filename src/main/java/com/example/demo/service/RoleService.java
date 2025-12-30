package com.example.demo.service;

import com.example.demo.exception.AppException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@lombok.RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class RoleService {
    private static final String USER_NOT_FOUND_CODE = "OD-02";
    private static final String ROLE_NOT_FOUND_CODE = "OD-04";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public void createRole(String roleName, String username) {
        log.info("Creating role {} for user {}", roleName, username);
        User user = userRepository.findById(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND_CODE));

        Role role = new Role();
        role.setRoleName(roleName);
        role.setUser(user);
        roleRepository.save(role);
        log.info("Role created successfully");
    }

    public String getRoleByUsername(String username) {
        return roleRepository.findByUserUsername(username)
                .stream()
                .findFirst()
                .map(Role::getRoleName)
                .orElseThrow(() -> new AppException(ROLE_NOT_FOUND_CODE));
    }
}
