package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for fetching a role.
 *
 * @param username the username to fetch the role for
 */
public record RoleFetch(
        @NotBlank(message = "VAL-USERNAME") String username) {
}
