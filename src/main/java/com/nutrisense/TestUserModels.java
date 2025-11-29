package com.nutrisense;

import static com.nutrisense.utils.PasswordUtil.hashPassword;

public class TestUserModels {
    public static void main(String[] args) {
        System.out.println("admin123 -> " + hashPassword("admin123"));
    System.out.println("12345 -> " + hashPassword("12345"));

    }
}
