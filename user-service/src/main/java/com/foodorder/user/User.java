package com.foodorder.user;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="users")
public class User {

	
	@Id
	private String id;
	
	@Column(unique=true, nullable=false)
	@Email
	private String email;
	
	
	@Column(nullable=false)
	@NotBlank
	private String passwordHash;
	
	@Column(nullable=false, name="full_name")
	@NotBlank
	private String fullName;
	
	@Column(nullable=false)
	@NotBlank
	private String phone;
	
	@Column(columnDefinition = "TEXT")
	private String address;
	
	@Column(name="created_at")
	private LocalDateTime createdAt;
	
	
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name="is_active")
	private Boolean isActive = true;
	
	
	public User() {
		this.id = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	
	public User(String email, String passwordHash, String fullName,String phone) {
		this();
		this.email =email;
		this.passwordHash = passwordHash;
		this.fullName = fullName;
		this.phone=phone;
	}
	
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPasswordHash() {
		return passwordHash;
	}


	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}


	public Boolean getIsActive() {
		return isActive;
	}


	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
	
}

