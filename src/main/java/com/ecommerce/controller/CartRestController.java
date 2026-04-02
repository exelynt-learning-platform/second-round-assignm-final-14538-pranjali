package com.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.dto.CartItemRequestDTO;
import com.ecommerce.dto.CartResponseDTO;
import com.ecommerce.service.CartService;

@RestController
@RequestMapping("/auth/cart")
public class CartRestController {

    private final CartService cartService;

    // Constructor Injection
    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    // GET CART BY USER
    @GetMapping("/get/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long userId) {
        return new ResponseEntity<>(
                cartService.getCartByUserId(userId),
                HttpStatus.OK
        );
    }

    // ADD ITEM TO CART
    @PostMapping("/add/{userId}")
    public ResponseEntity<CartResponseDTO> addItem(
            @PathVariable Long userId,
            @RequestBody CartItemRequestDTO dto) {

        return new ResponseEntity<>(
                cartService.addItemToCart(userId, dto),
                HttpStatus.CREATED   // ✅ better for POST
        );
    }

    // UPDATE ITEM QUANTITY
    @PutMapping("/update/{userId}")
    public ResponseEntity<CartResponseDTO> updateItem(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        return new ResponseEntity<>(
                cartService.updateItem(userId, productId, quantity),
                HttpStatus.OK
        );
    }

    // REMOVE ITEM
    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<CartResponseDTO> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        return new ResponseEntity<>(
                cartService.removeItem(userId, productId),
                HttpStatus.OK
        );
    }

    // CLEAR CART
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {

        cartService.clearCart(userId);

        return new ResponseEntity<>(
                "Cart cleared successfully",
                HttpStatus.OK
        );
    }
}