package com.ecommerce.service_impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.AuthResponseDTO;
import com.ecommerce.dto.UserLoginRequestDTO;
import com.ecommerce.dto.UserRegisterRequestDTO;
import com.ecommerce.dto.UserResponseDTO;
import com.ecommerce.entity.User;
import com.ecommerce.enums.Role;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.UnauthorizedException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtUtil;
import com.ecommerce.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public UserResponseDTO register(UserRegisterRequestDTO dto) {

		if (userRepository.findByUserEmail(dto.getEmail()).isPresent()) {
			throw new DuplicateResourceException("Email already registered");
		}

		if (userRepository.existsByUsername(dto.getUsername())) {
			throw new DuplicateResourceException("Username already taken");
		}

		User user = new User();
		user.setUsername(dto.getUsername());
		user.setUserEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(Role.USER);

		return mapToResponse(userRepository.save(user));
	}

	@Override
	public AuthResponseDTO login(UserLoginRequestDTO dto) {

		User user = userRepository
			    .findByUserEmailOrUsername(dto.getUsernameOrEmail(), dto.getUsernameOrEmail())
			    .orElse(null);

			if (user == null) {
			    throw new UnauthorizedException("Invalid credentials");
			}

			if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			    throw new UnauthorizedException("Invalid credentials");
			}

			String token = jwtUtil.generateToken(
				    user.getUsername() != null ? user.getUsername() : user.getUserEmail()
				);
	    AuthResponseDTO response = new AuthResponseDTO();
	    response.setToken(token);

	    return response;
	}

	@Override
	public UserResponseDTO getUserById(Long id) {
		return mapToResponse(
				userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")));
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserResponseDTO> responseList = new ArrayList<>();

		for (User user : users) {
			responseList.add(mapToResponse(user));
		}

		return responseList;
	}

	@Override
	public UserResponseDTO updateUser(Long id, UserRegisterRequestDTO dto) {

		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setUsername(dto.getUsername());
		user.setUserEmail(dto.getEmail());

		return mapToResponse(userRepository.save(user));
	}

	@Override
	public void deleteUser(Long id) {

		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found");
		}

		userRepository.deleteById(id);
	}

	// Convert Entity → DTO
	private UserResponseDTO mapToResponse(User user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setUserId(user.getUserId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getUserEmail());
		dto.setRole(Role.USER);
		return dto;
	}
}