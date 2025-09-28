package com.foodorder.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import com.foodorder.order.event.OrderCreatedEvent;
import com.foodorder.order.event.PaymentProcessedEvent;
import com.foodorder.order.event.PaymentStatus;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    private final UserServiceClient userServiceClient;
    
    private final RestaurantServiceClient restaurantServiceClient;
    
    private final RabbitTemplate rabbitTemplate;
    
 // Constants for exchanges and routing keys
    private static final String ORDER_EXCHANGE = "order.exchange";
    private static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    
    
    public OrderService(RabbitTemplate rabbitTemplate, UserServiceClient userServiceClient, RestaurantServiceClient restaurantServiceClient, OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
		this.userServiceClient = userServiceClient;
		this.restaurantServiceClient = restaurantServiceClient;
		this.rabbitTemplate = rabbitTemplate;
    	
    }
    
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
        
     // Publish order created event
        OrderCreatedEvent event = new OrderCreatedEvent(
            savedOrder.getId(),
            savedOrder.getUserId(),
            savedOrder.getRestaurantId(),
            savedOrder.getTotalAmount(),
            "CREDIT_CARD", // Default payment method, you can make this dynamic
            LocalDateTime.now()
        );
        
        rabbitTemplate.convertAndSend(
                ORDER_EXCHANGE,
                ORDER_CREATED_ROUTING_KEY,
                event
            );
        
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
    
    
    // Add method to listen to payment processed events
    @RabbitListener(queues = "order.status.queue")
    @Transactional
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        System.out.println("Received payment processed event for order: " + event.orderId());
        
        Optional<Order> orderOpt = orderRepository.findById(event.orderId());
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            if (event.status() == PaymentStatus.SUCCESS) {
                order.setStatus(OrderStatus.CONFIRMED);
                System.out.println("Order confirmed: " + order.getId());
            } else {
                order.setStatus(OrderStatus.CANCELLED);
                System.out.println("Order cancelled due to payment failure: " + order.getId());
            }
            
            orderRepository.save(order);
        }
    }
}