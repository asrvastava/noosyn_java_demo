package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for user sign-in.
 * 
 * @param username the username
 * @param password the password
 */
public record SignInRequest(
        @NotBlank(message = "VAL-USERNAME") String username,
        @NotBlank(message = "VAL-PASSWORD") String password) {
}
