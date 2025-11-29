package com.nutrisense.models.makanan;

public class Makanan {
    private String nama;
    private double kalori;
    private double protein;
    private double karbohidrat;
    private double lemak;
    private double serat;

    // Default constructor (untuk JSON parsing)
    public Makanan() {}

    // Constructor lengkap
    public Makanan(String nama, double kalori, double protein, 
                   double karbohidrat, double lemak, double serat) {
        this.nama = nama;
        this.kalori = kalori;
        this.protein = protein;
        this.karbohidrat = karbohidrat;
        this.lemak = lemak;
        this.serat = serat;
    }

    // Getters & Setters
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getKalori() { return kalori; }
    public void setKalori(double kalori) { this.kalori = kalori; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getKarbohidrat() { return karbohidrat; }
    public void setKarbohidrat(double karbohidrat) { this.karbohidrat = karbohidrat; }

    public double getLemak() { return lemak; }
    public void setLemak(double lemak) { this.lemak = lemak; }

    public double getSerat() { return serat; }
    public void setSerat(double serat) { this.serat = serat; }

    @Override
    public String toString() {
        return nama + " (" + kalori + " kkal)";
    }
}