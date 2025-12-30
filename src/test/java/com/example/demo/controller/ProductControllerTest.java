package com.example.demo.controller;

import com.example.demo.config.JwtAuthenticationFilter;
import com.example.demo.config.SecurityConfig;
import com.example.demo.dto.PaginatedResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import com.example.demo.util.ControllerUtil;
import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({ SecurityConfig.class, com.example.demo.config.CustomAuthenticationEntryPoint.class })
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

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
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Test Product", BigDecimal.valueOf(100.0));
        ProductResponse response = new ProductResponse(1L, "Test Product", BigDecimal.valueOf(100.0));

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post(ControllerUtil.PRODUCT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldGetAllProducts() throws Exception {
        ProductResponse product = new ProductResponse(1L, "Test Product", BigDecimal.valueOf(100.0));
        PaginatedResponse<ProductResponse> response = new PaginatedResponse<>(List.of(product), 1, 1, 1);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get(ControllerUtil.PRODUCT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldGetProductById() throws Exception {
        ProductResponse response = new ProductResponse(1L, "Test Product", BigDecimal.valueOf(100.0));

        when(productService.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get(ControllerUtil.PRODUCT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Updated Product", BigDecimal.valueOf(150.0));
        ProductResponse response = new ProductResponse(1L, "Updated Product", BigDecimal.valueOf(150.0));

        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put(ControllerUtil.PRODUCT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete(ControllerUtil.PRODUCT + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldFailCreateProductWhenUnauthorized() throws Exception {
        ProductRequest request = new ProductRequest("Test Product", BigDecimal.valueOf(100.0));

        mockMvc.perform(post(ControllerUtil.PRODUCT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldFailDeleteProductWhenUnauthorized() throws Exception {
        mockMvc.perform(delete(ControllerUtil.PRODUCT + "/1"))
                .andExpect(status().isForbidden());
    }
}
