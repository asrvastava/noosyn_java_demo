package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.AuthService;
import com.example.demo.dto.RoleRequest;
import com.example.demo.dto.RoleFetch;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.service.UserService;
import com.example.demo.service.RoleService;
import com.example.demo.util.ControllerUtil;

import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling authentication and role management operations.
 * Provides endpoints for user signup, login, role fetching, and role creation.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthService authService;

    private final org.springframework.context.MessageSource messageSource;

    /**
     * Registers a new user in the system.
     *
     * @param signupRequest the request object containing user signup details
     * @return a success message upon successful registration
     */
    @PostMapping(ControllerUtil.SIGNUP)
    public String signupUser(@jakarta.validation.Valid @RequestBody SignUpRequest signupRequest) {
        userService.signup(signupRequest);
        return messageSource.getMessage("OD-07", null,
                java.util.Locale.getDefault());
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param signinRequest the request object containing user credentials
     * @return a JWT token if authentication is successful
     */
    @PostMapping(ControllerUtil.LOGIN)
    public String signinUser(@jakarta.validation.Valid @RequestBody SignInRequest signinRequest) {
        return authService.authenticateAndGenerateToken(signinRequest.username(), signinRequest.password());
    }

    /**
     * Retrieves the role of a specific user.
     *
     * @param roleFetch the request object containing the username
     * @return the role of the user
     */
    @GetMapping(ControllerUtil.ROLE)
    public String getUserRole(@jakarta.validation.Valid @RequestBody RoleFetch roleFetch) {
        return roleService.getRoleByUsername(roleFetch.username());
    }

    /**
     * Creates a new role for a user.
     *
     * @param roleRequest the request object containing role details
     * @return a success message upon successful role creation
     */
    @PostMapping(ControllerUtil.ROLE_CREATE)
    public String createRole(@jakarta.validation.Valid @RequestBody RoleRequest roleRequest) {
        roleService.createRole(roleRequest.roleName(), roleRequest.username());
        return messageSource.getMessage("OD-01", null,
                java.util.Locale.getDefault());
    }
}
