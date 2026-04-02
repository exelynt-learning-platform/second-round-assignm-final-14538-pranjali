package com.ecommerce.service;

import com.ecommerce.dto.*;

public interface PaymentService {

    PaymentResponseDTO makePayment(PaymentRequestDTO dto);

    PaymentResponseDTO getPaymentById(Long id);

    PaymentResponseDTO updatePaymentStatus(Long id, String status);
}