package com.ecommerce.dto;

import com.ecommerce.enums.Role;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long userId;
    private String username;
    private String email;
    private Role role;
}