package com.example.demo.exception;

import lombok.Getter;

/**
 * Custom exception class for application-specific errors.
 * Stores an error code which can be mapped to a localized message.
 */
@Getter
public class AppException extends RuntimeException {
    private final String errorCode;

    /**
     * Constructs a new AppException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message   the detail message
     */
    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new AppException with the specified error code.
     * The error code is also used as the message.
     *
     * @param errorCode the error code
     */
    public AppException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
