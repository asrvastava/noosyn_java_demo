package com.example.demo.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        MvcResult result = mockMvc.perform(post(ControllerUtil.PRODUCT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProductResponse actualResponse = objectMapper.readValue(jsonResponse, ProductResponse.class);

        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.id());
        assertEquals("Test Product", actualResponse.name());
        assertEquals(BigDecimal.valueOf(100.0), actualResponse.price());

        verify(productService).createProduct(any(ProductRequest.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldGetAllProducts() throws Exception {
        ProductResponse product = new ProductResponse(1L, "Test Product", BigDecimal.valueOf(100.0));
        PaginatedResponse<ProductResponse> response = new PaginatedResponse<>(List.of(product), 1, 1, 1);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(get(ControllerUtil.PRODUCT))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        // We need a TypeReference for generic types
        PaginatedResponse<ProductResponse> actualResponse = objectMapper.readValue(jsonResponse, new com.fasterxml.jackson.core.type.TypeReference<PaginatedResponse<ProductResponse>>() {});

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.items().size());
        assertEquals("Test Product", actualResponse.items().get(0).name());
        assertEquals(1, actualResponse.totalItems());
        
        verify(productService).getAllProducts(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldGetProductById() throws Exception {
        ProductResponse response = new ProductResponse(1L, "Test Product", BigDecimal.valueOf(100.0));

        when(productService.getProductById(1L)).thenReturn(response);

        MvcResult result = mockMvc.perform(get(ControllerUtil.PRODUCT + "/1"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProductResponse actualResponse = objectMapper.readValue(jsonResponse, ProductResponse.class);

        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.id());
        assertEquals("Test Product", actualResponse.name());
        assertEquals(BigDecimal.valueOf(100.0), actualResponse.price());
        
        verify(productService).getProductById(1L);
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Updated Product", BigDecimal.valueOf(150.0));
        ProductResponse response = new ProductResponse(1L, "Updated Product", BigDecimal.valueOf(150.0));

        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(put(ControllerUtil.PRODUCT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProductResponse actualResponse = objectMapper.readValue(jsonResponse, ProductResponse.class);

        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.id());
        assertEquals("Updated Product", actualResponse.name());
        assertEquals(BigDecimal.valueOf(150.0), actualResponse.price());
        
        verify(productService).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void shouldDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        MvcResult result = mockMvc.perform(delete(ControllerUtil.PRODUCT + "/1"))
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());

        verify(productService).deleteProduct(1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldFailCreateProductWhenUnauthorized() throws Exception {
        ProductRequest request = new ProductRequest("Test Product", BigDecimal.valueOf(100.0));

        MvcResult result = mockMvc.perform(post(ControllerUtil.PRODUCT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldFailDeleteProductWhenUnauthorized() throws Exception {
        MvcResult result = mockMvc.perform(delete(ControllerUtil.PRODUCT + "/1"))
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
    }
}
