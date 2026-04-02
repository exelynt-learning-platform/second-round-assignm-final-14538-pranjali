package com.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO addProduct(ProductRequestDTO dto);

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);

    void deleteProduct(Long id);

    public List<ProductResponseDTO> searchProducts(String keyword, int page, int size);
    
    List<ProductResponseDTO> filterByPrice(BigDecimal min, BigDecimal max, int page, int size);
}