package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.error_dto.ApiErrorResponse;

import lombok.Builder;


@ControllerAdvice
@Builder
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(AppException ex) {

        String errorCode = ex.getErrorCode();
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(errorCode, null, Locale.getDefault());
        } catch (Exception e) {
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

        // Extract error code set in the DTO annotation
        String errorCode = ex.getFieldError().getDefaultMessage();

        // Get error message from message.properties
        String errorMessage = messageSource.getMessage(errorCode, null, Locale.getDefault());

        ApiErrorResponse body = ApiErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
