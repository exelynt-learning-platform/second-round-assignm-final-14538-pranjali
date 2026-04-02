package com.ecommerce.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "products")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(name = "product_name", nullable = false)
	@NotBlank(message = "Product name is required")
	private String productName;

	@Column(name = "description", nullable = false, length = 1000)
	@NotBlank(message = "Product description is required")
	private String description;

	@Column(name = "product_price", nullable = false, precision = 10, scale = 2)
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
	@NotNull(message = "Product price is required")
	private BigDecimal productPrice;

	@Column(name = "stock", nullable = false)
	@PositiveOrZero(message = "Stock cannot be negative")
	@NotNull(message = "Product stock is required")
	private Integer stock;

	@Column(name = "image_url")
	@jakarta.validation.constraints.Pattern(
			regexp = "^(http|https)://.*$", 
			message = "Invalid image URL")
	@NotBlank(message="image url is required")
	private String imageUrl;

	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL,
	fetch = FetchType.LAZY, orphanRemoval = true)
	private List<OrderItem> orderItems;
	
	@PrePersist
	public void setDefaultStock() {
	    if (stock == null) {
	        stock = 0;
	    }
	}

}
