package com.foodorder.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodorder.restaurant.MenuItem;
import com.foodorder.restaurant.MenuItemRepository;
import com.foodorder.restaurant.dto.MenuItemResponse;

@Service
public class MenuItemService {

	private final MenuItemRepository menuItemRepository;
	
	public MenuItemService(MenuItemRepository menuItemRepository) {
		this.menuItemRepository = menuItemRepository;
	}
	
	
	public List<MenuItemResponse> getMenuByRestaurant(String restaurantId) {
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId).stream()
                .map(this::convertToResponse)
                .toList();
    }
    
    public List<MenuItemResponse> getMenuByRestaurantAndCategory(String restaurantId, String category) {
        return menuItemRepository.findByRestaurantIdAndCategoryAndIsAvailableTrue(restaurantId, category).stream()
                .map(this::convertToResponse)
                .toList();
    }
    
    public List<MenuItemResponse> getVegetarianMenuByRestaurant(String restaurantId) {
        return menuItemRepository.findByRestaurantIdAndIsVegetarianTrueAndIsAvailableTrue(restaurantId).stream()
                .map(this::convertToResponse)
                .toList();
    }
    
    private MenuItemResponse convertToResponse(MenuItem menuItem) {
        return new MenuItemResponse(
            menuItem.getId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getCategory(),
            menuItem.getIsAvailable(),
            menuItem.getIsVegetarian(),
            menuItem.getIsVegan(),
            menuItem.getPreparationTime()
        );
    }
	
}
