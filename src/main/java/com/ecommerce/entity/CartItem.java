package com.ecommerce.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "cart_items")
@Data
@JsonIgnoreProperties({"cart"})

public class CartItem {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// many items belong to one cart
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	@NotNull(message = "Cart is required")
	@JsonIgnore
	@JsonIgnoreProperties({"cartItems"})
	@JsonBackReference
	@ToString.Exclude
	private Cart cart;
	
	// many items refer to one product
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	@NotNull(message = "Product is required")
	private Product product;

	// how many items user added
	@NotNull(message = "Quantity is required")
	@Min(value = 1, message = "Quantity must be at least 1")
	private Integer quantity;

	@NotNull(message = "Price cannot be null")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
	@Digits(integer = 10, fraction = 2, message = "invalid price format")
	private BigDecimal price;
}