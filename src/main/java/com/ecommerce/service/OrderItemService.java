package com.ecommerce.service;

import java.util.List;
import com.ecommerce.dto.*;

public interface OrderItemService {

    List<OrderItemResponseDTO> getItemsByOrderId(Long orderId);

    OrderItemResponseDTO getItemById(Long id);
}