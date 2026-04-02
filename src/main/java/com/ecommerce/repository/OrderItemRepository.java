package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Get items using Order object
    List<OrderItem> findByOrder(Order order);

    // Get items using orderId
    List<OrderItem> findByOrder_OrderId(Long orderId);
}