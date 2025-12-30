package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ApiErrorResponse(String errorCode, String message, LocalDateTime timestamp) {
}
