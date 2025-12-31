package com.example.demo.controller;

import com.example.demo.dto.PaginatedResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import com.example.demo.util.ControllerUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing products in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param request the request object containing product details
     * @return the created product response entity
     */
    @PostMapping(ControllerUtil.PRODUCT)
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of all products.
     *
     * @param pageable the pagination information
     * @return a paginated response containing a list of products
     */
    @GetMapping(ControllerUtil.PRODUCT)
    public ResponseEntity<PaginatedResponse<ProductResponse>> getAllProducts(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the ID of the product
     * @return the product response entity
     */
    @GetMapping(ControllerUtil.PRODUCT_BY_ID)
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Updates an existing product.
     *
     * @param id      the ID of the product to update
     * @param request the request object containing updated product details
     * @return the updated product response entity
     */
    @PutMapping(ControllerUtil.PRODUCT_BY_ID)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @return a response entity with no content
     */
    @DeleteMapping(ControllerUtil.PRODUCT_BY_ID)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
