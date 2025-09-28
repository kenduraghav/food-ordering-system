package com.foodorder.order;

public enum OrderStatus {
    PENDING,        // Order created, awaiting confirmation
    CONFIRMED,      // Order confirmed by restaurant  
    PREPARING,      // Restaurant is preparing food
    READY,          // Food ready for pickup/delivery
    OUT_FOR_DELIVERY, // Driver has picked up order
    DELIVERED,      // Order delivered to customer
    CANCELLED       // Order cancelled
}