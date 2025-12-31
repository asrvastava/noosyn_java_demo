package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleFetch(
                @NotBlank(message = com.example.demo.util.AppConstants.VAL_USERNAME) String username) {
}
