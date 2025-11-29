package com.nutrisense.services;

import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.Makanan;
import com.nutrisense.repositories.FoodJsonService;

public class SimpleFoodAnalyzer {
    private FoodJsonService foodRepo;

    public SimpleFoodAnalyzer() {
        this.foodRepo = new FoodJsonService();
    }

    // Untuk user umum: analyze SATU makanan + berat
    public GiziResult analyzeSingleFood(String namaMakanan, double beratGram) {
        Makanan makanan = foodRepo.findMakananByName(namaMakanan);
        
        if (makanan == null) {
            throw new IllegalArgumentException("Makanan tidak ditemukan: " + namaMakanan);
        }

        // Calculate nutrition based on weight (from per 100g to actual weight)
        double kalori = (makanan.getKalori() * beratGram) / 100;
        double protein = (makanan.getProtein() * beratGram) / 100;
        double karbohidrat = (makanan.getKarbohidrat() * beratGram) / 100;
        double lemak = (makanan.getLemak() * beratGram) / 100;
        double serat = (makanan.getSerat() * beratGram) / 100;

        return new GiziResult(kalori, protein, karbohidrat, lemak, serat);
    }

    // Untuk autocomplete di UI
    public java.util.List<String> searchMakanan(String query) {
        return foodRepo.searchMakananNames(query);
    }

    public FoodJsonService getFoodRepo() {
        return foodRepo;
    }
}