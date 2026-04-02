package com.ecommerce.service_impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.dto.OrderItemResponseDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    //GET ALL ITEMS BY ORDER ID
    @Override
    public List<OrderItemResponseDTO> getItemsByOrderId(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        List<OrderItemResponseDTO> responseList = new ArrayList<>();

        for (OrderItem item : items) {
            responseList.add(mapToDTO(item));
        }

        return responseList;
    }

    //GET SINGLE ITEM
    @Override
    public OrderItemResponseDTO getItemById(Long id) {

        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        return mapToDTO(item);
    }

    //ENTITY → DTO
    private OrderItemResponseDTO mapToDTO(OrderItem item) {

        OrderItemResponseDTO dto = new OrderItemResponseDTO();

        dto.setId(item.getOrderItemId()); 
        dto.setProductId(item.getProduct().getProductId());
        dto.setProductName(item.getProduct().getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setTotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

        return dto;
    }
}