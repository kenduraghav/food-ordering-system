// OrderCreatedEvent.java
package com.foodorder.order.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
    String orderId,
    String userId,
    String restaurantId,
    BigDecimal totalAmount,
    String paymentMethod,
    LocalDateTime timestamp
) {}

// PaymentProcessedEvent.java (for receiving)
