package com.foodorder.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Order> findByRestaurantIdOrderByCreatedAtDesc(String restaurantId);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);
}