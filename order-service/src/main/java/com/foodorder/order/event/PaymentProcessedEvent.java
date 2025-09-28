package com.foodorder.order.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentProcessedEvent(
    String paymentId,
    String orderId,
    String userId,
    BigDecimal amount,
    PaymentStatus status,
    String transactionId,
    String failureReason,
    LocalDateTime timestamp
) {}