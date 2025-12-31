package com.example.demo.service;

import com.example.demo.dto.PaginatedResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.exception.AppException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing product-related operations.
 * Handles the creation, retrieval, update, and deletion of products.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Creates a new product.
     *
     * @param request the request object containing product details
     * @return the created product response
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .build();
        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    /**
     * Retrieves all products with pagination.
     *
     * @param pageable the pagination information
     * @return a paginated response containing a list of products
     */
    @Transactional(readOnly = true)
    public PaginatedResponse<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = productsPage.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        return new PaginatedResponse<>(
                productResponses,
                productsPage.getNumber(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages());
    }

    /**
     * Retrieves a message product by its ID.
     *
     * @param id the ID of the product
     * @return the product response
     * @throws AppException if the product is not found
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException("ERR-404"));
        return mapToResponse(product);
    }

    /**
     * Updates an existing product.
     *
     * @param id      the ID of the product to update
     * @param request the request object containing updated product details
     * @return the updated product response
     * @throws AppException if the product is not found
     */
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException("ERR-404"));

        product.setName(request.name());
        product.setPrice(request.price());

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @throws AppException if the product is not found
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException("ERR-404"));
        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
