package com.foodorder.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodorder.restaurant.dto.MenuItemResponse;
import com.foodorder.restaurant.service.MenuItemService;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu")
public class MenuItemController {

	private final MenuItemService menuService;
	
	public MenuItemController(MenuItemService menuService) {
		this.menuService = menuService;
	}

	@GetMapping
	public ResponseEntity<List<MenuItemResponse>> getRestaurantMenu(@PathVariable String restaurantId) {
		List<MenuItemResponse> menuItems = menuService.getMenuByRestaurant(restaurantId);
		return ResponseEntity.ok(menuItems);
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<List<MenuItemResponse>> getMenuByCategory(@PathVariable String restaurantId,
			@PathVariable String category) {
		List<MenuItemResponse> menuItems = menuService.getMenuByRestaurantAndCategory(restaurantId, category);
		return ResponseEntity.ok(menuItems);
	}

	@GetMapping("/vegetarian")
	public ResponseEntity<List<MenuItemResponse>> getVegetarianMenu(@PathVariable String restaurantId) {
		List<MenuItemResponse> menuItems = menuService.getVegetarianMenuByRestaurant(restaurantId);
		return ResponseEntity.ok(menuItems);
	}
}
