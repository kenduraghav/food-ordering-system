package com.foodorder.user.dto;

import java.time.LocalDateTime;

public class UserResponse {
	
	private String id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    
    // Constructors
    public UserResponse() {}
    
    public UserResponse(String id, String email, String fullName, String phone, 
                       String address, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
