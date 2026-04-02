package com.ecommerce.service;

import java.util.List;
import com.ecommerce.dto.*;

public interface OrderService {

	public OrderResponseDTO placeOrder(Long userId, String address);

    OrderResponseDTO getOrderById(Long orderId);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO updateOrderStatus(Long orderId, String status);

    void cancelOrder(Long orderId);
}