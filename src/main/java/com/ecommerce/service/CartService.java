package com.ecommerce.service;

import com.ecommerce.dto.*;

public interface CartService {

    CartResponseDTO getCartByUserId(Long userId);

    CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO dto);

    CartResponseDTO updateItem(Long userId, Long productId, int quantity);

    CartResponseDTO removeItem(Long userId, Long productId);

    void clearCart(Long userId);
}