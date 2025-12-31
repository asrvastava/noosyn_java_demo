package com.example.demo.service;

import com.example.demo.exception.AppException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user roles.
 * Handles the creation and retrieval of user roles.
 */
@Service
@lombok.RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new role for a specific user.
     *
     * @param roleName the name of the role to create
     * @param username the username of the user to assign the role to
     */
    public void createRole(String roleName, String username) {
        log.info("Creating role {} for user {}", roleName, username);
        User user = userRepository.findById(username)
                .orElseThrow(() -> new AppException("OD-02"));

        Role role = new Role();
        role.setRoleName(roleName);
        role.setUser(user);
        roleRepository.save(role);
        log.info("Role created successfully");
    }

    /**
     * Retrieves the role name for a given username.
     *
     * @param username the username to retrieve the role for
     * @return the name of the role
     */
    public String getRoleByUsername(String username) {
        return roleRepository.findByUserUsername(username)
                .stream()
                .findFirst()
                .map(Role::getRoleName)
                .orElseThrow(() -> new AppException("OD-04"));
    }
}
