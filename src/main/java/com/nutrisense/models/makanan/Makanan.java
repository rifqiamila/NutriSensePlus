package com.nutrisense.models.makanan;

public class Makanan {
    private String nama;
    private double kalori;
    private double protein;
    private double karbohidrat;
    private double lemak;
    private double serat;

    public Makanan(String nama, double kalori, double protein, double karbohidrat, double lemak, double serat) {
        this.nama = nama;
        this.kalori = kalori;
        this.protein = protein;
        this.karbohidrat = karbohidrat;
        this.lemak = lemak;
        this.serat = serat;
    }

    public String getNama() {
        return nama;
    }

    public double getKalori() {
        return kalori;
    }

    public double getProtein() {
        return protein;
    }

    public double getKarbohidrat() {
        return karbohidrat;
    }

    public double getLemak() {
        return lemak;
    }

    public double getSerat() {
        return serat;
    }

    @Override
    public String toString() {
        return nama + " (Kal: " + kalori + ", Prot: " + protein +
                ", Karb: " + karbohidrat + ", Lemak: " + lemak + ", Serat: " + serat + ")";
    }
}