package com.foodorder.restaurant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, String> {

	List<MenuItem> findByRestaurantIdAndIsAvailableTrue(String restaurantId);
	
	List<MenuItem> findByRestaurantIdAndCategoryAndIsAvailableTrue(String restaurantId, String category);
    
    List<MenuItem> findByRestaurantIdAndIsVegetarianTrueAndIsAvailableTrue(String restaurantId);
}
