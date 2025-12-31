package com.example.demo.service;

import com.example.demo.exception.AppException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RoleService.
 * Verifies business logic for role management.
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    /**
     * Test case for creating a role.
     */
    @Test
    void shouldCreateRole() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));

        roleService.createRole("ADMIN", "testuser");

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    /**
     * Test case for failing to create a role when user is not found.
     */
    @Test
    void shouldFailCreateRoleWhenUserNotFound() {
        when(userRepository.findById("testuser")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> roleService.createRole("ADMIN", "testuser"));
        assertEquals("OD-02", ex.getErrorCode());
        verify(roleRepository, never()).save(any(Role.class));
    }

    /**
     * Test case for retrieving role by username.
     */
    @Test
    void shouldGetRoleByUsername() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleRepository.findByUserUsername("testuser")).thenReturn(List.of(role));

        String roleName = roleService.getRoleByUsername("testuser");

        assertEquals("ADMIN", roleName);
    }

    /**
     * Test case for failing to retrieve non-existent role.
     */
    @Test
    void shouldFailGetRoleByUsernameWhenRoleNotFound() {
        when(roleRepository.findByUserUsername("testuser")).thenReturn(Collections.emptyList());

        AppException ex = assertThrows(AppException.class, () -> roleService.getRoleByUsername("testuser"));
        assertEquals("OD-04", ex.getErrorCode());
    }
}
