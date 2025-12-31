package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.Builder;

/**
 * DTO representing an API error response.
 *
 * @param errorCode the error code
 * @param message   the error message
 * @param timestamp the timestamp of the error
 */
@Builder
public record ApiErrorResponse(String errorCode, String message, LocalDateTime timestamp) {
}
