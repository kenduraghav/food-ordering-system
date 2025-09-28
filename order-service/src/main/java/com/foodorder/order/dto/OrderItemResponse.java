package com.foodorder.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
    String id,
    String menuItemId,
    String menuItemName,
    Integer quantity,
    BigDecimal price,
    String specialInstructions
) {}