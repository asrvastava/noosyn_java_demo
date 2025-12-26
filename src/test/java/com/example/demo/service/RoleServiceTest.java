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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void shouldCreateRole() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));

        roleService.createRole("ADMIN", "testuser");

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void shouldFailCreateRoleWhenUserNotFound() {
        when(userRepository.findById("testuser")).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> roleService.createRole("ADMIN", "testuser"));
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void shouldGetRoleByUsername() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleRepository.findByUserUsername("testuser")).thenReturn(Optional.of(role));

        String roleName = roleService.getRoleByUsername("testuser");

        assertEquals("ADMIN", roleName);
    }

    @Test
    void shouldFailGetRoleByUsernameWhenRoleNotFound() {
        when(roleRepository.findByUserUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> roleService.getRoleByUsername("testuser"));
    }
}
