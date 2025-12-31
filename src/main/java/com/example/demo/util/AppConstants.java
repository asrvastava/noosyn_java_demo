package com.example.demo.util;

public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    // Role Names
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    // Error Codes (OD - Operational/Domain Errors)
    public static final String CODE_SUCCESS_ROLE_CREATE = "OD-01";
    public static final String CODE_USER_NOT_FOUND = "OD-02";
    public static final String CODE_INVALID_CREDENTIALS = "OD-03";
    public static final String CODE_ROLE_NOT_FOUND = "OD-04";
    public static final String CODE_UNAUTHORIZED_ACCESS = "OD-05";
    public static final String CODE_USER_ALREADY_EXISTS = "OD-06";
    public static final String CODE_SUCCESS_SIGNUP = "OD-07";

    // Error Codes (ERR - General Errors)
    public static final String CODE_INVALID_PRODUCT_DATA = "ERR-200";
    public static final String CODE_PRODUCT_NOT_FOUND = "ERR-404";
    public static final String CODE_VALIDATION_FAILED = "ERR-VALIDATION";
    public static final String CODE_INTERNAL_SERVER_ERROR = "ERR-500";

    // Validation Keys (VAL)
    public static final String VAL_USERNAME = "VAL-USERNAME";
    public static final String VAL_PASSWORD = "VAL-PASSWORD";
    public static final String VAL_ROLENAME = "VAL-ROLENAME";
    public static final String VAL_TOKEN = "VAL-TOKEN";
}
