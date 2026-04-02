package com.ecommerce.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {

		Optional<User> user;

		if (input.contains("@")) {
			user = userRepository.findByUserEmail(input);
		} else {
			user = userRepository.findByUsername(input);
		}

		return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}
