package com.foodorder.user;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foodorder.user.dto.UserRegistrationRequest;
import com.foodorder.user.dto.UserResponse;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserResponse registerUser(UserRegistrationRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		// For now, simple password hashing (we'll improve this later)
		String passwordHash = hashPassword(request.getPassword());

		User user = new User(request.getEmail(), passwordHash, request.getFullName(), request.getPhone());

		if (request.getAddress() != null) {
			user.setAddress(request.getAddress());
		}

		User savedUser = userRepository.save(user);

		return convertToResponse(savedUser);
	}

	public Optional<UserResponse> getUserById(String userId) {
		return userRepository.findById(userId).map(this::convertToResponse);
	}

	public Optional<UserResponse> getUserByEmail(String email) {
		return userRepository.findByEmail(email).map(this::convertToResponse);
	}

	// Simple password hashing (replace with BCrypt later)
	private String hashPassword(String password) {
		// TODO: Implement proper BCrypt hashing
		return "hashed_" + password;
	}

	private UserResponse convertToResponse(User user) {
		return new UserResponse(user.getId(), 
				user.getEmail(), 
				user.getFullName(), 
				user.getPhone(), 
				user.getAddress(),
				user.getCreatedAt());
	}

	public List<UserResponse> listAllUsers() {
		List<User> userEntities = userRepository.findAll();
		List<UserResponse>  users= userEntities.stream().map(this::convertToResponse).collect(toList());
		return users;
	}
}
