package com.bosch.ecommerce.repository;

import com.bosch.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByOwnerKey(String ownerKey);
}
