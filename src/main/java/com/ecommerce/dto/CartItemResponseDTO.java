package com.ecommerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal total;
}