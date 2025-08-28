package com.bosch.ecommerce.service;


import com.bosch.ecommerce.dto.AddToCartRequest;
import com.bosch.ecommerce.dto.CartItemResponse;
import com.bosch.ecommerce.dto.UpdateCartItemRequest;
import com.bosch.ecommerce.error.NotFoundException;
import com.bosch.ecommerce.model.Cart;
import com.bosch.ecommerce.model.CartItem;
import com.bosch.ecommerce.model.Product;
import com.bosch.ecommerce.repository.CartItemRepository;
import com.bosch.ecommerce.repository.CartRepository;
import com.bosch.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private String resolveOwnerKey() {
        return "anonymous";
    }

    private Cart getOrCreateCart(String ownerKey) {
        return cartRepository.findByOwnerKey(ownerKey)
                .map(c -> {
                    if (c.getItems() == null) c.setItems(new ArrayList<>());
                    return c;
                })
                .orElseGet(() -> {
                    Cart c = Cart.builder()
                            .ownerKey(ownerKey)
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(c);
                });
    }

    @Transactional
    public void add(AddToCartRequest req) {
        String owner = resolveOwnerKey();
        Cart cart = getOrCreateCart(owner);

        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        CartItem existing = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + req.quantity());
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(req.quantity())
                    .build();
            cart.getItems().add(item);
        }

        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart() {
        String owner = resolveOwnerKey();
        Cart cart = cartRepository.findByOwnerKey(owner)
                .orElse(Cart.builder().ownerKey(owner).items(new ArrayList<>()).build());

        return cart.getItems().stream().map(ci ->
                new CartItemResponse(
                        ci.getId(),
                        ci.getProduct().getId(),
                        ci.getProduct().getName(),
                        ci.getProduct().getPrice(),
                        ci.getQuantity(),
                        ci.getQuantity() * ci.getProduct().getPrice()
                )
        ).toList();
    }

    @Transactional
    public void updateItem(Long itemId, UpdateCartItemRequest req) {
        String owner = resolveOwnerKey();
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        if (!item.getCart().getOwnerKey().equals(owner)) {
            throw new NotFoundException("Cart item not found");
        }
        item.setQuantity(req.quantity());
    }

    @Transactional
    public void removeItem(Long itemId) {
        String owner = resolveOwnerKey();
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));
        if (!item.getCart().getOwnerKey().equals(owner)) {
            throw new NotFoundException("Cart item not found");
        }
        cartItemRepository.delete(item);
    }
}
