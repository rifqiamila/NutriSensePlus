package com.nutrisense.models.user;

public class Admin extends User {

    public Admin(String id, String username, String password) {
        super(id, username, password, "ADMIN");
    }

    @Override
    public String toString() {
        return "[ADMIN] " + username;
    }
}