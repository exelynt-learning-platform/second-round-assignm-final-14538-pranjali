package com.ecommerce.service_impl;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.dto.CartItemRequestDTO;
import com.ecommerce.dto.CartItemResponseDTO;
import com.ecommerce.dto.CartResponseDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CartService;

import jakarta.transaction.Transactional;

@Service
@Transactional
/*
 * @Transactional keeps Hibernate session OPEN 
 * Whenever you: Fetch LAZY data -> Convert to DTO 
 * -> Access relationships ->Method MUST be @Transactional
 */
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    //GET CART
    @Override
    public CartResponseDTO getCartByUserId(Long userId) {

        User user = getUser(userId);

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));

        return mapToResponse(cart);
    }

    //ADD ITEM
    @Override
    public CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO dto) {

        User user = getUser(userId);
        Product product = getProduct(dto.getProductId());

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));

        boolean found = false;

        for (CartItem item : cart.getCartItems()) {

            if (item.getProduct().getProductId().equals(product.getProductId())) {

                // update quantity
                item.setQuantity(item.getQuantity() + dto.getQuantity());

                // update price (always from DB)
                item.setPrice(product.getProductPrice());

                //calculate total
                BigDecimal total = product.getProductPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                found = true;
                break;
            }
        }

        // if item not found, create new
        if (!found) {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setCart(cart);

            // set price
            item.setPrice(product.getProductPrice());

            //calculate total
            BigDecimal total = product.getProductPrice()
                    .multiply(BigDecimal.valueOf(dto.getQuantity()));


            cart.getCartItems().add(item);
        }

        return mapToResponse(cartRepository.save(cart));
    }

    //UPDATE ITEM
    @Override
    public CartResponseDTO updateItem(Long userId, Long productId, int quantity) {

        User user = getUser(userId);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                return mapToResponse(cartRepository.save(cart));
            }
        }

        throw new ResourceNotFoundException("Product not in cart");
    }

    //REMOVE ITEM
    @Override
    public CartResponseDTO removeItem(Long userId, Long productId) {

        User user = getUser(userId);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getCartItems().removeIf(item -> item.getProduct().getProductId().equals(productId));

        return mapToResponse(cartRepository.save(cart));
    }

    //CLEAR CART
    @Override
    public void clearCart(Long userId) {

        User user = getUser(userId);

        if (!cartRepository.existsByUser(user)) {
            throw new ResourceNotFoundException("Cart not found");
        }

        cartRepository.deleteByUser(user);
    }

    // ================= HELPER METHODS =================

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    // ENTITY → DTO
    private CartResponseDTO mapToResponse(Cart cart) {

        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartId(cart.getId());

        List<CartItemResponseDTO> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {

            CartItemResponseDTO itemDTO = new CartItemResponseDTO();

            itemDTO.setProductId(item.getProduct().getProductId());
            itemDTO.setProductName(item.getProduct().getProductName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setProductPrice(item.getPrice()); // ✅ use stored price

            //calculate item total
            BigDecimal itemTotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            itemDTO.setTotal(itemTotal);

            total = total.add(itemTotal);

            items.add(itemDTO);
        }

        dto.setItems(items);
        dto.setTotalPrice(total);

        return dto;
    }
    }