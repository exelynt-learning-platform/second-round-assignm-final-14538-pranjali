package com.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private List<OrderItemResponseDTO> items;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String address;
    private LocalDateTime orderDate;
}