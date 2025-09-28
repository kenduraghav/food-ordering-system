// OrderCreatedEvent.java (received from Order Service)
package com.foodorder.payment.event;

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

// PaymentProcessedEvent.java (published by Payment Service)
