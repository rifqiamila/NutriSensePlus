package com.nutrisense.models.user;

public class Admin extends User {
    private String email;

    // Constructor dengan parameter lengkap
    public Admin() {
        super();
        this.role = UserRole.ADMIN;
        this.email = "";
    }

    public Admin(String username, String password, String email) {
        super(username, password, UserRole.ADMIN);
        this.email = email;
    }

    // Constructor default untuk super admin
    // public Admin() {
    //     super();
    //     this.role = UserRole.ADMIN;
    //     this.email = "admin@nutrisense.com";
    // }

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