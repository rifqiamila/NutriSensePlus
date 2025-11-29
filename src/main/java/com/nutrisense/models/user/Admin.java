package com.nutrisense.models.user;

import com.nutrisense.utils.PasswordUtil;

public class Admin extends User {
    private String email;

    // Constructor dengan parameter lengkap
    public Admin(String id, String username, String password, String email) {
        super(id, username, PasswordUtil.hashPassword(password), UserRole.ADMIN);
        this.email = email;
    }

    // Constructor default untuk super admin
    public Admin() {
        super("ADM001", "admin", PasswordUtil.hashPassword("admin123"), UserRole.ADMIN);
        this.email = "admin@nutrisense.com";
    }

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}