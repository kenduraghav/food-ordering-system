package com.foodorder.restaurant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String>{

	List<Restaurant> findByIsActiveTrue();
	
	List<Restaurant> findByCuisineTypeAndIsActiveTrue(String cuisineType);
	
	@Query("select e from Restaurant e where e.name ILIKE %:name% and e.isActive = true")
	List<Restaurant> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);
	
	@Query("select distinct e.cuisineType from Restaurant e where e.isActive = true")
	List<String>  findAllCuisineTypes();
}
