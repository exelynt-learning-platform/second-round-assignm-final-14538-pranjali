package com.ecommerce.service_impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductResponseDTO;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	//Constructor Injection
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	//ADD PRODUCT
	@Override
	public ProductResponseDTO addProduct(ProductRequestDTO dto) {

		Product product = new Product();
		product.setProductName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setProductPrice(dto.getPrice());
		product.setStock(dto.getStockQuantity());
		product.setImageUrl(dto.getImageUrl());

		return mapToResponse(productRepository.save(product));
	}

	//GET PRODUCT BY ID
	@Override
	public ProductResponseDTO getProductById(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		return mapToResponse(product);
	}

	//GET ALL PRODUCTS
	@Override
	public List<ProductResponseDTO> getAllProducts() {

		List<Product> products = productRepository.findAll();
		List<ProductResponseDTO> responseList = new ArrayList<>();

		for (Product product : products) {
			responseList.add(mapToResponse(product));
		}

		return responseList;
	}

	//UPDATE PRODUCT
	@Override
	public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		product.setProductName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setProductPrice(dto.getPrice());
		product.setStock(dto.getStockQuantity());
		product.setImageUrl(dto.getImageUrl());

		return mapToResponse(productRepository.save(product));
	}

	//DELETE PRODUCT
	@Override
	public void deleteProduct(Long id) {

		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found");
		}

		productRepository.deleteById(id);
	}

	//SEARCH PRODUCTS
	@Override
	public List<ProductResponseDTO> searchProducts(String keyword, int page, int size) {

	    Page<Product> productPage = productRepository
	            .findByProductNameContainingIgnoreCase(keyword, PageRequest.of(page, size));

	    List<ProductResponseDTO> responseList = new ArrayList<>();

	    for (Product product : productPage.getContent()) {
	        responseList.add(mapToResponse(product));
	    }

	    return responseList;
	}
	
	//ENTITY → DTO
	private ProductResponseDTO mapToResponse(Product product) {

		ProductResponseDTO dto = new ProductResponseDTO();
		dto.setId(product.getProductId());
		dto.setName(product.getProductName());
		dto.setDescription(product.getDescription());
		dto.setPrice(product.getProductPrice());
		dto.setStockQuantity(product.getStock());
		dto.setImageUrl(product.getImageUrl());

		return dto;
	}

	@Override
	public List<ProductResponseDTO> filterByPrice(BigDecimal min, BigDecimal max, int page, int size) {

	    // create pagination
	    Pageable pageable = PageRequest.of(page, size);

	    // get data from DB
	    Page<Product> productPage = productRepository.findByProductPriceBetween(min, max, pageable);

	    // convert to DTO manually
	    List<ProductResponseDTO> dtoList = new ArrayList<>();

	    for (Product product : productPage.getContent()) {
	        ProductResponseDTO dto = new ProductResponseDTO();

	        dto.setId(product.getProductId());
	        dto.setName(product.getProductName());
	        dto.setDescription(product.getDescription());
	        dto.setPrice(product.getProductPrice());
	        dto.setStockQuantity(product.getStock());
	        dto.setImageUrl(product.getImageUrl());

	        dtoList.add(dto);
	    }

	    return dtoList;
	}
}