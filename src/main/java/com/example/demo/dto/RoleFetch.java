package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleFetch(
        @NotBlank(message = "VAL-USERNAME") String username) {
}
