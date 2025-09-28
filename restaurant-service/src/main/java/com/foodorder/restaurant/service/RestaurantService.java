package com.foodorder.restaurant.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foodorder.restaurant.Restaurant;
import com.foodorder.restaurant.RestaurantRepository;
import com.foodorder.restaurant.dto.RestaurantResponse;

@Service
public class RestaurantService {

	
	private final RestaurantRepository restaurantRepository;
	
	
	public RestaurantService(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}
	
	
	
	public List<RestaurantResponse> getAllActiveRestaurants() {
		return restaurantRepository.findByIsActiveTrue().stream().map(this::convertToResponse).toList();
	}
	
	public Optional<RestaurantResponse> getRestaurantById(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .filter(restaurant -> restaurant.getIsActive())
                .map(this::convertToResponse);
    }
    
    public List<RestaurantResponse> getRestaurantsByCuisine(String cuisineType) {
        return restaurantRepository.findByCuisineTypeAndIsActiveTrue(cuisineType).stream()
                .map(this::convertToResponse)
               .toList();
    }
    
    public List<RestaurantResponse> searchRestaurantsByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name).stream()
                .map(this::convertToResponse)
                .toList();
    }
    
    public List<String> getAllCuisineTypes() {
        return restaurantRepository.findAllCuisineTypes();
    }



	private RestaurantResponse convertToResponse(Restaurant restaurant) {
		return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getCuisineType(), 
				restaurant.getAddress(), 
				restaurant.getPhone(), 
				restaurant.getDescription(),
				restaurant.getDeliveryFee(), 
				restaurant.getMinOrderAmount(), 
				restaurant.getAverageDeliveryTime(),
				restaurant.getIsActive(),
				restaurant.getCreatedAt());
	}
	
}
