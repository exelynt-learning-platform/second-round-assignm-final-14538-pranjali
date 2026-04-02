package com.ecommerce.dto;

import lombok.Data;

@Data
public class UserLoginRequestDTO {

	private String usernameOrEmail;
	private String password;
}
