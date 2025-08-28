package com.bosch.ecommerce.product.service;

import com.bosch.ecommerce.error.NotFoundException;
import com.bosch.ecommerce.model.Product;
import com.bosch.ecommerce.repository.ProductRepository;
import com.bosch.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    @Test
    void findById_ok() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        when(repo.findById(1L)).thenReturn(Optional.of(Product.builder().id(1L).name("X").price(1.0).description("d").build()));
        ProductService svc = new ProductService(repo);
        assertEquals(1L, svc.findById(1L).id());
    }

    @Test
    void findById_notFound() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        when(repo.findById(1L)).thenReturn(Optional.empty());
        ProductService svc = new ProductService(repo);
        assertThrows(NotFoundException.class, () -> svc.findById(1L));
    }

    @Test
    void findAll_pagination() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Page<Product> page = new PageImpl<>(List.of(
                Product.builder().id(1L).name("A").price(10.0).description("d").build()
        ), PageRequest.of(0,10), 1);
        when(repo.findAll(
                Mockito.<Specification<Product>>any(),
                Mockito.any(Pageable.class)
        )).thenReturn(page);

        ProductService svc = new ProductService(repo);
        var resp = svc.findAll(0,10,null,null,null,null,null);
        assertEquals(1, resp.totalElements());
        assertEquals(1, resp.content().size());
    }
}
