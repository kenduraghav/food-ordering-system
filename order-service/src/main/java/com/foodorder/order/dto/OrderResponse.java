package com.foodorder.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.foodorder.order.OrderStatus;

public record OrderResponse(
    String id,
    String userId,
    String restaurantId,
    String restaurantName, // From restaurant service
    OrderStatus status,
    BigDecimal totalAmount,
    String deliveryAddress,
    String customerPhone,
    String specialInstructions,
    LocalDateTime estimatedDeliveryTime,
    LocalDateTime createdAt,
    List<OrderItemResponse> items
) {}

// OrderItemResponse.java
