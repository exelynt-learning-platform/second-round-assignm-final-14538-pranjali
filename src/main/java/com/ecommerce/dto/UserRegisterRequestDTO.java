package com.ecommerce.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDTO {
    private String username;
    private String email;
    private String password;
}