package com.ecommerce.service_impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.dto.OrderItemResponseDTO;
import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    //PLACE ORDER
    @Override
    public OrderResponseDTO placeOrder(Long userId, String address) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address); //correct

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getProductPrice());

            BigDecimal itemTotal = cartItem.getProduct()
                    .getProductPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            total = total.add(itemTotal);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        //clear cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }
    //GET ORDER BY ID
    @Override
    public OrderResponseDTO getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return mapToResponse(order);
    }

    // GET USER ORDERS
    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

        List<OrderResponseDTO> list = new ArrayList<>();

        for (Order order : orders) {
            list.add(mapToResponse(order));
        }

        return list;
    }

    // GET ALL ORDERS
    @Override
    public List<OrderResponseDTO> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> list = new ArrayList<>();

        for (Order order : orders) {
            list.add(mapToResponse(order));
        }

        return list;
    }

    //UPDATE ORDER STATUS
    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));

        return mapToResponse(orderRepository.save(order));
    }

    // CANCEL ORDER
    @Override
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    // ================= DTO MAPPING =================

    private OrderResponseDTO mapToResponse(Order order) {

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setPaymentStatus(order.getPaymentStatus().name());
        dto.setAddress(order.getAddress());
        dto.setOrderDate(order.getOrderDate());

        List<OrderItemResponseDTO> items = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();

            //set ID
            itemDTO.setId(item.getOrderItemId());

            itemDTO.setProductId(item.getProduct().getProductId());
            itemDTO.setProductName(item.getProduct().getProductName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());

            //set TOTAL
            BigDecimal total = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            itemDTO.setTotal(total);

            items.add(itemDTO);
        }

        dto.setItems(items);

        return dto;
    }
    }
