package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for user sign-up.
 * 
 * @param username the username
 * @param password the password
 */
public record SignUpRequest(
        @NotBlank(message = "VAL-USERNAME") String username,
        @NotBlank(message = "VAL-PASSWORD") String password) {
}
