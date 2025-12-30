package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
        @NotBlank(message = "VAL-USERNAME") String username,
        @NotBlank(message = "VAL-ROLENAME") String roleName) {
}
