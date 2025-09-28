package com.foodorder.order.client;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.foodorder.order.dto.external.MenuItemResponse;
import com.foodorder.order.dto.external.RestaurantResponse;

@Component
public class RestaurantServiceClient {

	private final RestTemplate restTemplate;

	@Value("${services.restaurant-service.url}")
	private String restaurantServiceUrl;

	public RestaurantServiceClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	
	public Optional<RestaurantResponse> getRestaurantById(String restaurantId) {
        try {
            String url = restaurantServiceUrl + "/api/restaurants/" + restaurantId;
            RestaurantResponse restaurant = restTemplate.getForObject(url, RestaurantResponse.class);
            return Optional.ofNullable(restaurant);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch restaurant: " + restaurantId, e);
        }
    }
    
    public List<MenuItemResponse> getRestaurantMenu(String restaurantId) {
        try {
            String url = restaurantServiceUrl + "/api/restaurants/" + restaurantId + "/menu";
            MenuItemResponse[] menuItems = restTemplate.getForObject(url, MenuItemResponse[].class);
            return menuItems != null ? Arrays.asList(menuItems) : List.of();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch menu for restaurant: " + restaurantId, e);
        }
    }
}
