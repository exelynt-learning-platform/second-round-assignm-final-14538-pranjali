package com.ecommerce.service;

import java.util.List;

import com.ecommerce.dto.AuthResponseDTO;
import com.ecommerce.dto.UserLoginRequestDTO;
import com.ecommerce.dto.UserRegisterRequestDTO;
import com.ecommerce.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO register(UserRegisterRequestDTO dto);

    AuthResponseDTO login(UserLoginRequestDTO dto);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRegisterRequestDTO dto);

    void deleteUser(Long id);
}