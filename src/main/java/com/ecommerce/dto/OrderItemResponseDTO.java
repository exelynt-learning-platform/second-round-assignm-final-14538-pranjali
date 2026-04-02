package com.ecommerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponseDTO {
	private Long id;
	private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
}