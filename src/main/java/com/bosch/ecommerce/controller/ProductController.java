package com.bosch.ecommerce.controller;

import com.bosch.ecommerce.dto.ProductResponse;
import com.bosch.ecommerce.paging.PageResponse;
import com.bosch.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public PageResponse<ProductResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String category,
            @RequestParam(name = "minPrice", required = false) String minPrice,
            @RequestParam(name = "maxPrice", required = false) String maxPrice,
            @RequestParam(name = "q", required = false) String q
    ) {
        return productService.findAll(page, size, sort, category, minPrice, maxPrice, q);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable Long id) {
        return productService.findById(id);
    }
}
