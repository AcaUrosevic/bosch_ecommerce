package com.bosch.ecommerce.dto;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        double price,
        int quantity,
        double lineTotal
) {
}
