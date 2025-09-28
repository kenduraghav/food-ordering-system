package com.foodorder.restaurant.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodorder.restaurant.dto.RestaurantResponse;
import com.foodorder.restaurant.service.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;
	
	public RestaurantController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}
	
	
	@GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        List<RestaurantResponse> restaurants = restaurantService.getAllActiveRestaurants();
        return ResponseEntity.ok(restaurants);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable String id) {
        Optional<RestaurantResponse> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cuisine/{cuisineType}")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCuisine(
            @PathVariable String cuisineType) {
        List<RestaurantResponse> restaurants = restaurantService.getRestaurantsByCuisine(cuisineType);
        return ResponseEntity.ok(restaurants);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurants(
            @RequestParam String name) {
        List<RestaurantResponse> restaurants = restaurantService.searchRestaurantsByName(name);
        return ResponseEntity.ok(restaurants);
    }
    
    @GetMapping("/cuisines")
    public ResponseEntity<List<String>> getAllCuisineTypes() {
        List<String> cuisines = restaurantService.getAllCuisineTypes();
        return ResponseEntity.ok(cuisines);
    }
    
    // Health check
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Restaurant Service is running!");
    }
	
}
