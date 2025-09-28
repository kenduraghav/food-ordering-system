package com.foodorder.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(name = "menu_item_id", nullable = false)
    private String menuItemId;
    
    @Column(name = "menu_item_name", nullable = false)
    private String menuItemName; // Snapshot of item name at time of order
    
    @Column(nullable = false)
    @NotNull
    private Integer quantity;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal price; // Snapshot of item price at time of order
    
    @Column(name = "special_instructions")
    private String specialInstructions;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public OrderItem() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }
    
    public OrderItem(String menuItemId, String menuItemName, Integer quantity, BigDecimal price) {
        this();
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }
    
    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}