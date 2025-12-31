package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating or managing a role.
 *
 * @param username the username
 * @param roleName the name of the role
 */
public record RoleRequest(
        @NotBlank(message = "VAL-USERNAME") String username,
        @NotBlank(message = "VAL-ROLENAME") String roleName) {
}
