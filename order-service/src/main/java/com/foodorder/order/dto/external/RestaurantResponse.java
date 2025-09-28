package com.foodorder.order.dto.external;

import java.time.LocalDateTime;

public record RestaurantResponse(
    String id,
    String name,
    String cuisineType,
    String address,
    String phone,
    String description,
    Double deliveryFee,
    Double minOrderAmount,
    Integer averageDeliveryTime,
    Boolean isActive,
    LocalDateTime createdAt
) {}

// MenuItemResponse.java (for service communication)
