package com.ecommerce.service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;

public interface CartItemService {
	
	CartItem addItem(Cart cart, Product product, int quantity);

    CartItem updateItemQuantity(Cart cart, Product product, int quantity);

    void removeItem(Cart cart, Product product);

    void clearCartItems(Cart cart);

}
