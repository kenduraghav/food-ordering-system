package com.foodorder.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodorder.user.UserService;
import com.foodorder.user.dto.UserRegistrationRequest;
import com.foodorder.user.dto.UserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {

		try {
			UserResponse user = userService.registerUser(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(user);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	
	@GetMapping
	public ResponseEntity<List<UserResponse>> listAllUsers(){
		List<UserResponse> users = userService.listAllUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
		Optional<UserResponse> user = userService.getUserById(id);
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
		Optional<UserResponse> user = userService.getUserByEmail(email);
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Health check endpoint
	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("User Service is running!");
	}
}
