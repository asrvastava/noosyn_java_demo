package com.example.demo.util;


public class ControllerUtil {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN = "/signin";
    public static final String SIGNUP = "/signup";
    public static final String ROLE_CREATE = "/role_create";
    public static final String ROLE = "/role";
    public static final String PRODUCT = "/product";

    private ControllerUtil() {
        // Private constructor to hide the implicit public one
    }
    
    // Utility methods for controllers can be added here
}