package com.foodorder.restaurant.dto;

public record MenuItemResponse( String id,
	    String name,
	    String description,
	    Double price,
	    String category,
	    Boolean isAvailable,
	    Boolean isVegetarian,
	    Boolean isVegan,
	    Integer preparationTime) {

}
