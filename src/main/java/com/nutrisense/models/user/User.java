package com.nutrisense.models.user;

import java.time.LocalDateTime;

public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    protected UserRole role;
    protected LocalDateTime createdAt;
    protected boolean isActive;

    public enum UserRole {
        SISWA,
        DAPUR_MBG, 
        ADMIN,
        UMUM
    }

    // Constructor
    public User(String id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}