package com.ecommerce.service;

import com.ecommerce.dto.*;

public interface AuthService {

    AuthResponseDTO login(UserLoginRequestDTO dto);

    AuthResponseDTO refreshToken(String refreshToken);
}