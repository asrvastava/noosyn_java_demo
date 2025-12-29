package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.AuthService;
import com.example.demo.dto.RoleRequest;
import com.example.demo.dto.RoleFetch;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.service.UserService;
import com.example.demo.service.RoleService;
import com.example.demo.exception.AppException;
import com.example.demo.util.ControllerUtil;

@RestController
public class ApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;


    @PostMapping(ControllerUtil.SIGNUP)
    public String signupUser(@RequestBody SignUpRequest signupRequest) {
        userService.signup(signupRequest);
        return "User signed up successfully";
    }
    
    @PostMapping(ControllerUtil.LOGIN)
    public String signinUser(@RequestBody SignInRequest signinRequest) {
        return authService.authenticateAndGenerateToken(signinRequest.username(), signinRequest.password());
    }


    @GetMapping(ControllerUtil.ROLE)
    public String getUserRole(@RequestBody RoleFetch roleFetch, @RequestHeader("Authorization") String authHeader) {
        validateToken(authHeader);
        return roleService.getRoleByUsername(roleFetch.username());
    }

    @PostMapping(ControllerUtil.ROLE_CREATE)
    public String createRole(@RequestBody RoleRequest roleRequest, @RequestHeader("Authorization") String authHeader) {
        validateToken(authHeader);
        roleService.createRole(roleRequest.roleName(), roleRequest.username());
        return "Role created successfully";
    }

    private void validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(ControllerUtil.TOKEN_PREFIX)) {
             throw new AppException("OD-05");
        }
        String token = authHeader.substring(ControllerUtil.TOKEN_PREFIX.length());
        if (!authService.validateToken(token)) {
             throw new AppException("OD-05");
        }
    }
}
