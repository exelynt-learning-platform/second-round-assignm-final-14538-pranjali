package com.ecommerce.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="carts")
@Data
public class Cart {
	    
	    @Id
	    @Column(name = "id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    // one cart belongs to one user
	    @OneToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false, unique = true)
	    @NotNull(message = "User is required")
	    @ToString.Exclude
	    private User user;

	    // one cart contains many items
	    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, 
	               fetch = FetchType.LAZY, orphanRemoval = true)
	    @ToString.Exclude
	    @JsonIgnoreProperties({"cart"})
	    @OrderBy("id ASC")
	    @JsonManagedReference
	    private List<CartItem> cartItems;

	    @Column(name = "total_amount", precision = 10, scale = 2)
	    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount cannot be negative")
	    private BigDecimal totalAmount;

	    // set default total
	    @PrePersist
	    public void setDefaultTotal() {
	        if (totalAmount == null) {
	            totalAmount = BigDecimal.ZERO;
	        }
	    }
	}
