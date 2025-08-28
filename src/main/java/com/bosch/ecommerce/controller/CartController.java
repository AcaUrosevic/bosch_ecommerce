package com.bosch.ecommerce.controller;

import com.bosch.ecommerce.dto.AddToCartRequest;
import com.bosch.ecommerce.dto.CartItemResponse;
import com.bosch.ecommerce.dto.UpdateCartItemRequest;
import com.bosch.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid @RequestBody AddToCartRequest req) {
        cartService.add(req);
    }

    @GetMapping
    public List<CartItemResponse> get() {
        return cartService.getCart();
    }

    @PutMapping("/item/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody UpdateCartItemRequest req) {
        cartService.updateItem(id, req);
    }

    @DeleteMapping("/item/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        cartService.removeItem(id);
    }
}
