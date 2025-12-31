package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Custom authentication entry point to handle authentication failures.
 * Returns a uniform JSON error response when authentication fails.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    /**
     * Commences an authentication scheme.
     *
     * @param request       that resulted in an AuthenticationException
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        String errorCode = "VAL-TOKEN";
        String errorMessage = messageSource.getMessage(errorCode, null, Locale.getDefault());

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
