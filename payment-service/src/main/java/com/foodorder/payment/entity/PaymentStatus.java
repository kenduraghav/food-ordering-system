package com.foodorder.payment.entity;

public enum PaymentStatus {
    PENDING,     // Payment initiated
    PROCESSING,  // Being processed by gateway
    SUCCESS,     // Payment successful
    FAILED,      // Payment failed
    REFUNDED,    // Payment refunded
    CANCELLED    // Payment cancelled
}

// PaymentMethod.java
