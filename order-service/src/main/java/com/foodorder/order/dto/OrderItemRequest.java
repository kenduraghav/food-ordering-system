package com.foodorder.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
    @NotBlank(message = "Menu item ID is required")
    String menuItemId,
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer quantity,
    
    String specialInstructions
) {}

// OrderResponse.java
