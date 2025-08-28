package com.bosch.ecommerce.dto;

import com.bosch.ecommerce.model.Product;

public record ProductResponse(
        Long id, String name, double price, String description, String category
) {
    public static ProductResponse from (Product product) {
        return new ProductResponse(
                product.getId(), product.getName(),
                product.getPrice(), product.getDescription(),
                product.getCategory()
        );
    }
}
