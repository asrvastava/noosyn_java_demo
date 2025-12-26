package com.example.demo.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final String errorCode;

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
