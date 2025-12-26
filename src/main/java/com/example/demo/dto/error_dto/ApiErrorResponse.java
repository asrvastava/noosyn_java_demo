package com.example.demo.dto.error_dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
}
