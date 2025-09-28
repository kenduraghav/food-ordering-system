// UserResponse.java (for service communication)
package com.foodorder.order.dto.external;

import java.time.LocalDateTime;

public record UserResponse(
    String id,
    String email,
    String fullName,
    String phone,
    String address,
    LocalDateTime createdAt
) {}

// RestaurantResponse.java (for service communication)
