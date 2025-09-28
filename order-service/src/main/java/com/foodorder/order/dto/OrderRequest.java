// OrderRequest.java
package com.foodorder.order.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record OrderRequest(
    @NotBlank(message = "User ID is required")
    String userId,
    
    @NotBlank(message = "Restaurant ID is required") 
    String restaurantId,
    
    @NotBlank(message = "Delivery address is required")
    String deliveryAddress,
    
    @NotBlank(message = "Customer phone is required")
    String customerPhone,
    
    String specialInstructions,
    
    @NotEmpty(message = "Order items cannot be empty")
    List<OrderItemRequest> items
) {}

// OrderItemRequest.java  
