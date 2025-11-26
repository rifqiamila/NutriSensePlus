package com.nutrisense.models.user;

public class User {
    private String id;
    private String name;
    private String role;
    private String password;

    public User(String role) {
        this.role = role;
    }

    // getters & setters
    public String getId(){return id;} public void setId(String id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getRole(){return role;} public void setRole(String role){this.role=role;}
    public String getPassword(){return password;} public void setPassword(String password){this.password=password;}

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', role='" + role + "', password='" + password + "'}";
    }
}
