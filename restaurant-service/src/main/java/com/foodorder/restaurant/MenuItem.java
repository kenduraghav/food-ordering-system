package com.foodorder.restaurant;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "menu_items")
public class MenuItem {

	 @Id
	    private String id;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "restaurant_id", nullable = false)
	    private Restaurant restaurant;
	    
	    @Column(nullable = false)
	    @NotBlank
	    private String name;
	    
	    @Column(columnDefinition = "TEXT")
	    private String description;
	    
	    @Column(nullable = false)
	    @NotNull
	    private Double price;
	    
	    @Column
	    private String category; // appetizer, main, dessert, drink
	    
	    @Column(name = "is_available")
	    private Boolean isAvailable = true;
	    
	    @Column(name = "is_vegetarian")
	    private Boolean isVegetarian = false;
	    
	    @Column(name = "is_vegan")
	    private Boolean isVegan = false;
	    
	    @Column(name = "preparation_time")
	    private Integer preparationTime; // in minutes
	    
	    @Column(name = "created_at")
	    private LocalDateTime createdAt;
	    
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;
	    
	    // Default constructor
	    public MenuItem() {
	        this.id = UUID.randomUUID().toString();
	        this.createdAt = LocalDateTime.now();
	        this.updatedAt = LocalDateTime.now();
	    }
	    
	    // Constructor
	    public MenuItem(Restaurant restaurant, String name, String description, Double price) {
	        this();
	        this.restaurant = restaurant;
	        this.name = name;
	        this.description = description;
	        this.price = price;
	    }
	    
	    // Getters and Setters
	    public String getId() { return id; }
	    public void setId(String id) { this.id = id; }
	    
	    public Restaurant getRestaurant() { return restaurant; }
	    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
	    
	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }
	    
	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; }
	    
	    public Double getPrice() { return price; }
	    public void setPrice(Double price) { this.price = price; }
	    
	    public String getCategory() { return category; }
	    public void setCategory(String category) { this.category = category; }
	    
	    public Boolean getIsAvailable() { return isAvailable; }
	    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
	    
	    public Boolean getIsVegetarian() { return isVegetarian; }
	    public void setIsVegetarian(Boolean isVegetarian) { this.isVegetarian = isVegetarian; }
	    
	    public Boolean getIsVegan() { return isVegan; }
	    public void setIsVegan(Boolean isVegan) { this.isVegan = isVegan; }
	    
	    public Integer getPreparationTime() { return preparationTime; }
	    public void setPreparationTime(Integer preparationTime) { this.preparationTime = preparationTime; }
	    
	    public LocalDateTime getCreatedAt() { return createdAt; }
	    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	    
	    public LocalDateTime getUpdatedAt() { return updatedAt; }
	    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	    
	    @PreUpdate
	    public void preUpdate() {
	        this.updatedAt = LocalDateTime.now();
	    }
}
