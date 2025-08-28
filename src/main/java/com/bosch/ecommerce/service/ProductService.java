package com.bosch.ecommerce.service;

import com.bosch.ecommerce.error.NotFoundException;
import com.bosch.ecommerce.paging.PageResponse;
import com.bosch.ecommerce.dto.ProductResponse;
import com.bosch.ecommerce.model.Product;
import com.bosch.ecommerce.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @PostConstruct
    void seed() {
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    Product.builder().name("Mouse").price(19.99).description("Optical mouse").category("peripherals").build(),
                    Product.builder().name("Keyboard").price(49.99).description("Mechanical keyboard").category("peripherals").build(),
                    Product.builder().name("Monitor").price(199.99).description("24-inch IPS").category("display").build()
            ));
        }
    }

    public PageResponse<ProductResponse> findAll(
            int page, int size, String sort, String category, String qMinPrice, String qMaxPrice, String q
    ) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Specification<Product> spec = Specification.allOf();

        if (category != null && !category.isBlank()) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("category"), category));
        }
        if (q != null && !q.isBlank()) {
            String like = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, cq, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")), like),
                            cb.like(cb.lower(root.get("description")), like)
                    )
            );
        }
        if (qMinPrice != null) {
            double min = Double.parseDouble(qMinPrice);
            spec = spec.and((root, cq, cb) -> cb.ge(root.get("price"), min));
        }
        if (qMaxPrice != null) {
            double max = Double.parseDouble(qMaxPrice);
            spec = spec.and((root, cq, cb) -> cb.le(root.get("price"), max));
        }

        Page<Product> pageData = productRepository.findAll(spec, pageable);
        List<ProductResponse> content = pageData.getContent().stream().map(ProductResponse::from).toList();
        return PageResponse.of(content, pageData);
    }

    public ProductResponse findById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product %d not found".formatted(id)));
        return ProductResponse.from(p);
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) return Sort.by(Sort.Direction.ASC, "id");
        String[] parts = sort.split(",");
        String field = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }
}
