package com.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	//Check if product already exists in user's cart
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
	
//	Get all items inside a cart
	List<CartItem> findByCart(Cart cart);
	
//	Clear cart after order
	void deleteByCart(Cart cart);

//	Remove specific product from cart
    void deleteByCartAndProduct(Cart cart, Product product);
}

/*
 *CartItemService = helper (internal)
 *CartService = main (talks to controller)
 *Inner services → Entity
 *Main services → DTO
 *
 *Because Cart already “owns” CartItems
 *Cart = Parent
 *CartItem = Child
 *
CartItem is a child entity of Cart, so all operations are handled via CartController
to maintain proper REST design.


👉 You never interact with CartItem alone
👉 You always interact through Cart

 * Why not return DTO from CartItemService? 
 * Because CartItemService is alower-level internal service.
 * DTO mapping should be handled at service layer
 * exposed to controller to maintain separation of concerns.”
 */