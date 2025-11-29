package com.nutrisense.models.makanan;

public class MenuMakanan {
    private Makanan makanan;
    private double beratGram;

    public MenuMakanan(Makanan makanan, double beratGram) {
        this.makanan = makanan;
        this.beratGram = beratGram;
    }

    // Getters & Setters
    public Makanan getMakanan() { return makanan; }
    public void setMakanan(Makanan makanan) { this.makanan = makanan; }

    public double getBeratGram() { return beratGram; }
    public void setBeratGram(double beratGram) { this.beratGram = beratGram; }

    // Helper methods - hitung gizi berdasarkan berat
    public double getKalori() {
        return (makanan.getKalori() * beratGram) / 100.0;
    }

    public double getProtein() {
        return (makanan.getProtein() * beratGram) / 100.0;
    }

    public double getKarbohidrat() {
        return (makanan.getKarbohidrat() * beratGram) / 100.0;
    }

    public double getLemak() {
        return (makanan.getLemak() * beratGram) / 100.0;
    }

    public double getSerat() {
        return (makanan.getSerat() * beratGram) / 100.0;
    }

    @Override
    public String toString() {
        return makanan.getNama() + " - " + beratGram + "g";
    }
}