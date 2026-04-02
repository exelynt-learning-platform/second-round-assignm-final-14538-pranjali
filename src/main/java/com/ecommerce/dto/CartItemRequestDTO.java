package com.ecommerce.dto;

import lombok.Data;

@Data
public class CartItemRequestDTO {
    private Long productId;
    private Integer quantity;
}