package com.foodorder.order.event;

// Separate enum in Order Service
public enum PaymentStatus {
    PENDING, PROCESSING, SUCCESS, FAILED, REFUNDED, CANCELLED
}