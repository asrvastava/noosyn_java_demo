package com.example.demo.util;

public class ControllerUtil {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ENDPOINT_PREFIX = "/api/v1/";
    public static final String LOGIN = ENDPOINT_PREFIX + "signin";
    public static final String SIGNUP = ENDPOINT_PREFIX + "signup";
    public static final String ROLE_CREATE = ENDPOINT_PREFIX + "role_create";
    public static final String ROLE = ENDPOINT_PREFIX + "role";
    public static final String PRODUCT = ENDPOINT_PREFIX + "product";

    private ControllerUtil() {
        // Private constructor to hide the implicit public one
    }

    // Utility methods for controllers can be added here
}