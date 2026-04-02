package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Page<Order> findByUser(User user, Pageable pageable);
	
	List<Order> findByUserOrderByOrderDateDesc(User user);
}
