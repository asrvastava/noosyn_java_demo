package com.example.demo.exception;

import com.example.demo.dto.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldHandleAppException() {
        AppException ex = new AppException(com.example.demo.util.AppConstants.CODE_USER_NOT_FOUND);
        when(messageSource.getMessage(eq(com.example.demo.util.AppConstants.CODE_USER_NOT_FOUND), any(),
                any(Locale.class))).thenReturn("User not found");

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleAppException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(com.example.demo.util.AppConstants.CODE_USER_NOT_FOUND, response.getBody().errorCode());
        assertEquals("User not found", response.getBody().message());
    }

    @Test
    void shouldHandleValidationException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field",
                com.example.demo.util.AppConstants.CODE_INVALID_PRODUCT_DATA);

        when(ex.getFieldError()).thenReturn(fieldError);
        when(messageSource.getMessage(eq(com.example.demo.util.AppConstants.CODE_INVALID_PRODUCT_DATA), any(),
                any(Locale.class))).thenReturn("Invalid product data");

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(com.example.demo.util.AppConstants.CODE_INVALID_PRODUCT_DATA, response.getBody().errorCode());
        assertEquals("Invalid product data", response.getBody().message());
    }
}
