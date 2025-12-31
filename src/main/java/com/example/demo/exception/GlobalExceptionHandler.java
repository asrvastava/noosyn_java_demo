package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.ApiErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(AppException ex) {
        log.error("AppException occurred: {}", ex.getMessage());
        String errorCode = ex.getErrorCode();
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(errorCode, new Object[] {}, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            errorMessage = ex.getMessage();
        }

        ApiErrorResponse body = ApiErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        // Extract error code set in the DTO annotation
        String errorCode = java.util.Optional.ofNullable(ex.getFieldError())
                .map(org.springframework.validation.FieldError::getDefaultMessage)
                .orElse("ERR-VALIDATION");

        // Get error message from message.properties
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(errorCode, new Object[] {}, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            errorMessage = messageSource.getMessage("ERR-VALIDATION",
                    null, "Validation failed", Locale.getDefault());
        }

        ApiErrorResponse body = ApiErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        ApiErrorResponse body = ApiErrorResponse.builder()
                .errorCode("ERR-500")
                .message(ex.getMessage()) // Leaking internal error for debugging
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
