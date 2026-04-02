package com.ecommerce.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.User;
import com.ecommerce.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private AuthenticationManager authManager;
	private JwtUtil jwtUtil;

	public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public String login(@RequestBody User user) {

		authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getPassword()));

		return jwtUtil.generateToken(user.getUserEmail());
	}
}