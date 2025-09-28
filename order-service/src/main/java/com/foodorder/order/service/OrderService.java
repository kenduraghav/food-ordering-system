package com.foodorder.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodorder.order.Order;
import com.foodorder.order.OrderItem;
import com.foodorder.order.OrderRepository;
import com.foodorder.order.OrderStatus;
import com.foodorder.order.client.RestaurantServiceClient;
import com.foodorder.order.client.UserServiceClient;
import com.foodorder.order.dto.OrderItemRequest;
import com.foodorder.order.dto.OrderItemResponse;
import com.foodorder.order.dto.OrderRequest;
import com.foodorder.order.dto.OrderResponse;
import com.foodorder.order.dto.external.MenuItemResponse;
import com.foodorder.order.dto.external.RestaurantResponse;
import com.foodorder.order.dto.external.UserResponse;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    @Autowired
    private RestaurantServiceClient restaurantServiceClient;
    
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        
        // 1. Validate user exists
        validateUser(request);
        
        RestaurantResponse restaurant = validateRestaurant(request);
        
        // 3. Get restaurant menu and validate items
        List<MenuItemResponse> menuItems = restaurantServiceClient.getRestaurantMenu(request.restaurantId());
        Map<String, MenuItemResponse> menuItemMap = menuItems.stream()
                .collect(Collectors.toMap(MenuItemResponse::id, Function.identity()));
        
        // 4. Create order
        Order order = new Order(
            request.userId(),
            request.restaurantId(),
            request.deliveryAddress(),
            request.customerPhone()
        );
        
        if (request.specialInstructions() != null) {
            order.setSpecialInstructions(request.specialInstructions());
        }
        
        // 5. Add order items with validation
        for (OrderItemRequest itemRequest : request.items()) {
            MenuItemResponse menuItem = menuItemMap.get(itemRequest.menuItemId());
            
            if (menuItem == null) {
                throw new RuntimeException("Menu item not found: " + itemRequest.menuItemId());
            }
            
            if (!menuItem.isAvailable()) {
                throw new RuntimeException("Menu item not available: " + menuItem.name());
            }
            
            OrderItem orderItem = new OrderItem(
                itemRequest.menuItemId(),
                menuItem.name(),
                itemRequest.quantity(),
                BigDecimal.valueOf(menuItem.price())
            );
            
            if (itemRequest.specialInstructions() != null) {
                orderItem.setSpecialInstructions(itemRequest.specialInstructions());
            }
            
            order.addOrderItem(orderItem);
        }
        
        // 6. Calculate total amount
        order.calculateTotalAmount();
        
        // 7. Set estimated delivery time
        LocalDateTime estimatedDelivery = LocalDateTime.now()
                .plusMinutes(restaurant.averageDeliveryTime() != null ? restaurant.averageDeliveryTime() : 30);
        order.setEstimatedDeliveryTime(estimatedDelivery);
        
        // 8. Save order
        Order savedOrder = orderRepository.save(order);
        
        return convertToResponse(savedOrder, restaurant.name());
    }

	private RestaurantResponse validateRestaurant(OrderRequest request) {
		// 2. Validate restaurant exists and is active
        RestaurantResponse restaurant = restaurantServiceClient.getRestaurantById(request.restaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + request.restaurantId()));
        
        if (!restaurant.isActive()) {
            throw new RuntimeException("Restaurant is not active: " + request.restaurantId());
        }
		return restaurant;
	}

	private void validateUser(OrderRequest request) {
		userServiceClient.getUserById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.userId()));
	}
    
    public Optional<OrderResponse> getOrderById(String orderId) {
        return orderRepository.findById(orderId).map(order -> {
            // Get restaurant name for response
            String restaurantName = restaurantServiceClient.getRestaurantById(order.getRestaurantId())
                    .map(RestaurantResponse::name)
                    .orElse("Unknown Restaurant");
            
            return convertToResponse(order, restaurantName);
        });
    }
    
    public List<OrderResponse> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(order -> {
                    String restaurantName = restaurantServiceClient.getRestaurantById(order.getRestaurantId())
                            .map(RestaurantResponse::name)
                            .orElse("Unknown Restaurant");
                    return convertToResponse(order, restaurantName);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional
    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        
        String restaurantName = restaurantServiceClient.getRestaurantById(order.getRestaurantId())
                .map(RestaurantResponse::name)
                .orElse("Unknown Restaurant");
        
        return convertToResponse(updatedOrder, restaurantName);
    }
    
    private OrderResponse convertToResponse(Order order, String restaurantName) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                    item.getId(),
                    item.getMenuItemId(),
                    item.getMenuItemName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getSpecialInstructions()
                ))
                .collect(Collectors.toList());
        
        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getRestaurantId(),
            restaurantName,
            order.getStatus(),
            order.getTotalAmount(),
            order.getDeliveryAddress(),
            order.getCustomerPhone(),
            order.getSpecialInstructions(),
            order.getEstimatedDeliveryTime(),
            order.getCreatedAt(),
            itemResponses
        );
    }
}