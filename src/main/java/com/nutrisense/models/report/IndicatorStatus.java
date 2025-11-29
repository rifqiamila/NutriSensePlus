package com.nutrisense.models.report;

public enum IndicatorStatus {
    HIJAU("Hijau", "Gizi Seimbang", "#27ae60", "✅"),
    KUNING("Kuning", "Gizi Kurang", "#f39c12", "⚠️"),
    MERAH("Merah", "Gizi Tidak Seimbang", "#e74c3c", "❌");

    private final String label;
    private final String deskripsi;
    private final String colorCode;
    private final String emoji;

    IndicatorStatus(String label, String deskripsi, String colorCode, String emoji) {
        this.label = label;
        this.deskripsi = deskripsi;
        this.colorCode = colorCode;
        this.emoji = emoji;
    }

    // Getters
    public String getLabel() {
        return label;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getEmoji() {
        return emoji;
    }

    // Helper methods
    public boolean isSeimbang() {
        return this == HIJAU;
    }

    public boolean isPerhatian() {
        return this == KUNING;
    }

    public boolean isKritis() {
        return this == MERAH;
    }

    public static IndicatorStatus fromValue(String value) {
        for (IndicatorStatus status : values()) {
            if (status.name().equalsIgnoreCase(value) || 
                status.getLabel().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return KUNING; // Default safe value
    }

    @Override
    public String toString() {
        return emoji + " " + label + " - " + deskripsi;
    }
}