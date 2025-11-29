package com.nutrisense.models.laporan;

import java.time.LocalDateTime;

public class Feedback {
    private String id;
    private String siswaId;
    private String menuId;
    private String dapurId;
    private int rating; // 1-5 stars
    private String komentar;
    private LocalDateTime createdAt;
    private boolean isActive;

    // Constructors
    public Feedback() {}

    public Feedback(String id, String siswaId, String menuId, String dapurId, 
                   int rating, String komentar) {
        this.id = id;
        this.siswaId = siswaId;
        this.menuId = menuId;
        this.dapurId = dapurId;
        this.rating = rating;
        this.komentar = komentar;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSiswaId() { return siswaId; }
    public void setSiswaId(String siswaId) { this.siswaId = siswaId; }

    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getDapurId() { return dapurId; }
    public void setDapurId(String dapurId) { this.dapurId = dapurId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { 
        // Validation: rating between 1-5
        if (rating < 1) this.rating = 1;
        else if (rating > 5) this.rating = 5;
        else this.rating = rating;
    }

    public String getKomentar() { return komentar; }
    public void setKomentar(String komentar) { this.komentar = komentar; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // Helper methods
    public String getRatingStars() {
        return "⭐".repeat(rating) + "☆".repeat(5 - rating);
    }

    public boolean isPositiveFeedback() {
        return rating >= 4; // 4-5 stars considered positive
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + id + '\'' +
                ", siswaId='" + siswaId + '\'' +
                ", menuId='" + menuId + '\'' +
                ", rating=" + getRatingStars() +
                ", createdAt=" + createdAt.toLocalDate() +
                '}';
    }
}