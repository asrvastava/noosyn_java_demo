package com.example.demo.dto;

import java.util.List;

/**
 * Generic DTO for paginated responses.
 *
 * @param <T>         the type of items in the response
 * @param items       the list of items
 * @param currentPage the current page number
 * @param totalItems  the total number of items
 * @param totalPages  the total number of pages
 */
public record PaginatedResponse<T>(
                List<T> items,
                int currentPage,
                long totalItems,
                int totalPages) {
}
