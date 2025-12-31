package com.example.demo.dto;

import java.math.BigDecimal;

/**
 * DTO representing a product response.
 *
 * @param id    the ID of the product
 * @param name  the name of the product
 * @param price the price of the product
 */
public record ProductResponse(Long id, String name, BigDecimal price) {
}
