package com.foodorder.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of(
                "error", "User service is currently unavailable",
                "message", "Please try again later",
                "timestamp", LocalDateTime.now(),
                "service", "user-service"
            ));
    }

    @GetMapping("/restaurants")
    public ResponseEntity<Map<String, Object>> restaurantServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of(
                "error", "Restaurant service is currently unavailable", 
                "message", "Please try again later",
                "timestamp", LocalDateTime.now(),
                "service", "restaurant-service"
            ));
    }

    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> orderServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of(
                "error", "Order service is currently unavailable",
                "message", "Please try again later. Your order may still be processing.",
                "timestamp", LocalDateTime.now(),
                "service", "order-service"
            ));
    }
}