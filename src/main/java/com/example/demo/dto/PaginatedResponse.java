package com.example.demo.dto;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> items,
        int currentPage,
        long totalItems,
        int totalPages) {
}
