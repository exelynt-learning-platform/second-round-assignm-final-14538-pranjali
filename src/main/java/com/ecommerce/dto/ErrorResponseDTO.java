package com.ecommerce.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {
	private String message;
	private int status;
	private long timestamp;
	

}
