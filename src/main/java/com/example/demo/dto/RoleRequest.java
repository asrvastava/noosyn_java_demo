package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
                @NotBlank(message = com.example.demo.util.AppConstants.VAL_USERNAME) String username,
                @NotBlank(message = com.example.demo.util.AppConstants.VAL_ROLENAME) String roleName) {
}
