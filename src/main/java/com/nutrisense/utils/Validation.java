package com.nutrisense.utils;

import com.nutrisense.exceptions.ValidationException;

public class Validation {

    // ==========================
    // STRING REQUIRED
    // ==========================
    public static void requireNotEmpty(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new ValidationException(fieldName + " tidak boleh kosong");
        }
    }

    // ==========================
    // NUMBER MUST BE POSITIVE
    // ==========================
    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " harus lebih dari 0");
        }
    }

    // ==========================
    // NUMBER MINIMUM VALUE
    // ==========================
    public static void requireMin(double value, double min, String fieldName) {
        if (value < min) {
            throw new ValidationException(fieldName + " minimal adalah " + min);
        }
    }

    // ==========================
    // SIMPLE EMAIL CHECK
    // ==========================
    public static void requireEmail(String email) {
        requireNotEmpty(email, "Email");

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Format email tidak valid");
        }
    }

    // ==========================
    // PASSWORD POLICY
    // ==========================
    public static void requirePassword(String password) {
        requireNotEmpty(password, "Password");

        if (password.length() < 6) {
            throw new ValidationException("Password minimal 6 karakter");
        }
    }
}
