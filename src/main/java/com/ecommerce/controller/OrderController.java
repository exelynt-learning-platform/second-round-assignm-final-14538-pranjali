package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.service.OrderService;

@RestController
@RequestMapping("/auth/orders")
public class OrderController {

    private final OrderService orderService;

    // Constructor Injection
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // PLACE ORDER
    @PostMapping("/place/{userId}")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @PathVariable Long userId,
            @RequestParam String address) {

        OrderResponseDTO response = orderService.placeOrder(userId, address);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //GET ORDER BY ID
    @GetMapping("/get/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {

        return new ResponseEntity<>(
                orderService.getOrderById(orderId),
                HttpStatus.OK
        );
    }

    //GET ORDERS BY USER
    @GetMapping("get/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable Long userId) {

        return new ResponseEntity<>(
                orderService.getOrdersByUserId(userId),
                HttpStatus.OK
        );
    }

    //GET ALL ORDERS (ADMIN)
    @GetMapping("/getAll")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {

        return new ResponseEntity<>(
                orderService.getAllOrders(),
                HttpStatus.OK
        );
    }

    //UPDATE ORDER STATUS
    @PutMapping("/update/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        return new ResponseEntity<>(
                orderService.updateOrderStatus(orderId, status),
                HttpStatus.OK
        );
    }

    // CANCEL ORDER
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {

        orderService.cancelOrder(orderId);

        return new ResponseEntity<>(
                "Order cancelled successfully",
                HttpStatus.OK
        );
    }
}