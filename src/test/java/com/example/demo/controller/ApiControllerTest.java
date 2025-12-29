package com.example.demo.controller;

import com.example.demo.dto.RoleFetch;
import com.example.demo.dto.RoleRequest;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.ControllerUtil;
import com.example.demo.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
@Import(SecurityConfig.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSignupUser() throws Exception {
        SignUpRequest request = new SignUpRequest("testuser", "password");
        doNothing().when(userService).signup(any(SignUpRequest.class));

        mockMvc.perform(post(ControllerUtil.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User signed up successfully"));
    }

    @Test
    void shouldSigninUser() throws Exception {
        SignInRequest request = new SignInRequest("testuser", "password");
        when(authService.authenticateAndGenerateToken("testuser", "password")).thenReturn("mock-token");

        mockMvc.perform(post(ControllerUtil.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-token"));
    }

    @Test
    void shouldGetUserRole() throws Exception {
        RoleFetch request = new RoleFetch("testuser");
        String token = "valid-token";
        
        when(authService.validateToken(token)).thenReturn(true);
        when(roleService.getRoleByUsername("testuser")).thenReturn("ADMIN");

        mockMvc.perform(get(ControllerUtil.ROLE)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("ADMIN"));
    }

    @Test
    void shouldFailGetUserRoleWhenUnauthorized() throws Exception {
        RoleFetch request = new RoleFetch("testuser");
        String token = "invalid-token";

        when(authService.validateToken(token)).thenReturn(false);

        mockMvc.perform(get(ControllerUtil.ROLE)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // GlobalExceptionHandler handles AppException as BAD_REQUEST
    }

    @Test
    void shouldCreateRole() throws Exception {
        RoleRequest request = new RoleRequest("ADMIN", "testuser");
        String token = "valid-token";

        when(authService.validateToken(token)).thenReturn(true);
        doNothing().when(roleService).createRole("ADMIN", "testuser");

        mockMvc.perform(post(ControllerUtil.ROLE_CREATE)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Role created successfully"));
    }
}
