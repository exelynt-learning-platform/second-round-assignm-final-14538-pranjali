package com.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartResponseDTO {
    private Long cartId;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalPrice;
}