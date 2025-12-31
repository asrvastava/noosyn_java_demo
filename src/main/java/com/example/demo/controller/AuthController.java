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

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthService authService;

    private final org.springframework.context.MessageSource messageSource;

    @PostMapping(ControllerUtil.SIGNUP)
    public String signupUser(@jakarta.validation.Valid @RequestBody SignUpRequest signupRequest) {
        userService.signup(signupRequest);
        return messageSource.getMessage("OD-07", null,
                java.util.Locale.getDefault());
    }

    @PostMapping(ControllerUtil.LOGIN)
    public String signinUser(@jakarta.validation.Valid @RequestBody SignInRequest signinRequest) {
        return authService.authenticateAndGenerateToken(signinRequest.username(), signinRequest.password());
    }

    @GetMapping(ControllerUtil.ROLE)
    public String getUserRole(@jakarta.validation.Valid @RequestBody RoleFetch roleFetch) {
        return roleService.getRoleByUsername(roleFetch.username());
    }

    @PostMapping(ControllerUtil.ROLE_CREATE)
    public String createRole(@jakarta.validation.Valid @RequestBody RoleRequest roleRequest) {
        roleService.createRole(roleRequest.roleName(), roleRequest.username());
        return messageSource.getMessage("OD-01", null,
                java.util.Locale.getDefault());
    }
}
