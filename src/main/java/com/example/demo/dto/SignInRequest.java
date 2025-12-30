package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "VAL-USERNAME") String username,
        @NotBlank(message = "VAL-PASSWORD") String password) {
}
