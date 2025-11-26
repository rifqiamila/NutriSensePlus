package com.nutrisense.models.makanan;

public class GiziResult {
    private double totalKalori;
    private double totalProtein;
    private double totalKarbohidrat;
    private double totalLemak;
    private double totalSerat;

    public GiziResult(double totalKalori, double totalProtein, double totalKarbohidrat, double totalLemak, double totalSerat) {
        this.totalKalori = totalKalori;
        this.totalProtein = totalProtein;
        this.totalKarbohidrat = totalKarbohidrat;
        this.totalLemak = totalLemak;
        this.totalSerat = totalSerat;
    }

    public double  getTotalKalori() {
        return totalKalori;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public double getTotalKarbohidrat() {
        return totalKarbohidrat;
    }

    public double getTotalLemak() {
        return totalLemak;
    }

    public double getTotalSerat() {
        return totalSerat;
    }

    @Override
    public String toString() {
        return "Total Gizi -> Kalori: " + totalKalori +
                ", Protein: " + totalProtein +
                ", Karbohidrat: " + totalKarbohidrat +
                ", Lemak: " + totalLemak +
                ", Serat: " + totalSerat;
    }
}
