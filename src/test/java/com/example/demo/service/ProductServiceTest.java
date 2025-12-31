package com.example.demo.service;

import com.example.demo.dto.PaginatedResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.exception.AppException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProduct() {
        ProductRequest request = new ProductRequest("Test Product", BigDecimal.TEN);
        Product product = Product.builder().id(1L).name("Test Product").price(BigDecimal.TEN).build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Test Product", response.name());
        assertEquals(BigDecimal.TEN, response.price());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldGetAllProducts() {
        Product product = Product.builder().id(1L).name("Test Product").price(BigDecimal.TEN).build();
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(pageable)).thenReturn(page);

        PaginatedResponse<ProductResponse> response = productService.getAllProducts(pageable);

        assertNotNull(response);
        assertEquals(1, response.items().size());
        assertEquals("Test Product", response.items().get(0).name());
    }

    @Test
    void shouldGetProductById() {
        Product product = Product.builder().id(1L).name("Test Product").price(BigDecimal.TEN).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals("Test Product", response.name());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundById() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> productService.getProductById(1L));
        assertEquals(com.example.demo.util.AppConstants.CODE_PRODUCT_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void shouldUpdateProduct() {
        ProductRequest request = new ProductRequest("Updated Product", BigDecimal.valueOf(20));
        Product product = Product.builder().id(1L).name("Test Product").price(BigDecimal.TEN).build();
        Product updatedProduct = Product.builder().id(1L).name("Updated Product").price(BigDecimal.valueOf(20)).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse response = productService.updateProduct(1L, request);

        assertNotNull(response);
        assertEquals("Updated Product", response.name());
        assertEquals(BigDecimal.valueOf(20), response.price());
    }

    @Test
    void shouldDeleteProduct() {
        Product product = Product.builder().id(1L).name("Test Product").price(BigDecimal.TEN).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> productService.deleteProduct(1L));
        assertEquals(com.example.demo.util.AppConstants.CODE_PRODUCT_NOT_FOUND, ex.getErrorCode());
        verify(productRepository, never()).delete(any(Product.class));
    }
}
