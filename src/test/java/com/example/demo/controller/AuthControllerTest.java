package com.example.demo.controller;

import com.example.demo.dto.RoleFetch;
import com.example.demo.dto.RoleRequest;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.ControllerUtil;
import com.example.demo.util.JwtUtil;
import com.example.demo.config.JwtAuthenticationFilter;
import com.example.demo.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({ SecurityConfig.class, com.example.demo.config.CustomAuthenticationEntryPoint.class })
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    void shouldSignupUser() throws Exception {
        SignUpRequest request = new SignUpRequest("testuser", "password");
        doNothing().when(userService).signup(any(SignUpRequest.class));

        MvcResult result = mockMvc.perform(post(ControllerUtil.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("User signed up successfully", result.getResponse().getContentAsString());

        verify(userService).signup(any(SignUpRequest.class));
    }

    @Test
    void shouldSigninUser() throws Exception {
        SignInRequest request = new SignInRequest("testuser", "password");
        when(authService.authenticateAndGenerateToken("testuser", "password")).thenReturn("mock-token");

        MvcResult result = mockMvc.perform(post(ControllerUtil.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("mock-token", result.getResponse().getContentAsString());

        verify(authService).authenticateAndGenerateToken("testuser", "password");
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "ADMIN")
    void shouldGetUserRole() throws Exception {
        RoleFetch request = new RoleFetch("testuser");

        when(roleService.getRoleByUsername("testuser")).thenReturn("ADMIN");

        MvcResult result = mockMvc.perform(get(ControllerUtil.ROLE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("ADMIN", result.getResponse().getContentAsString());

        verify(roleService).getRoleByUsername("testuser");
    }

    @Test
    void shouldFailGetUserRoleWhenUnauthorized() throws Exception {
        RoleFetch request = new RoleFetch("testuser");

        MvcResult result = mockMvc.perform(get(ControllerUtil.ROLE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldCreateRole() throws Exception {
        RoleRequest request = new RoleRequest("testuser", "ADMIN");

        doNothing().when(roleService).createRole("ADMIN", "testuser");

        MvcResult result = mockMvc.perform(post(ControllerUtil.ROLE_CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Role created successfully", result.getResponse().getContentAsString());

        verify(roleService).createRole("ADMIN", "testuser");
    }

    @Test
    void shouldReturnForbiddenWhenTokenNotProvidedForProtectedResource() throws Exception {
        RoleRequest request = new RoleRequest("testuser", "ADMIN");

        MvcResult result = mockMvc.perform(post(ControllerUtil.ROLE_CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
        
        String jsonResponse = result.getResponse().getContentAsString();
        // Assuming ApiErrorResponse or similar structure, but here we can just check the string or map
        // Since we don't have the exact error response class handy in the test context (it might be ApiErrorResponse), 
        // let's read it as a Map or JsonNode to assert fields.
        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        
        assertEquals("Token not provided", jsonNode.get("message").asText());
        assertEquals("VAL-TOKEN", jsonNode.get("errorCode").asText());
    }
}
