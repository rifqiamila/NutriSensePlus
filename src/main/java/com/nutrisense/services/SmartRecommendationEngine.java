package com.nutrisense.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nutrisense.models.makanan.GiziResult;
//import com.nutrisense.repositories.MealPlanRepository;

public class SmartRecommendationEngine {

    // Ambang batas biar ga merah
    private static final int MIN_KALORI = 500;
    private static final int MAX_KALORI = 800;
    private static final int MIN_PROTEIN = 20;
    private static final int MAX_LEMAK = 30;

//    private final MealPlanRepository mealRepo = new MealPlanRepository();

    // REKOMENDASI HARIAN (biar ga merah)
    public List<String> recommend(GiziResult gizi) {
        List<String> rec = new ArrayList<>();

        if (gizi.getTotalKalori() < MIN_KALORI)
            rec.add("Kalori rendah: tambahkan nasi, roti, atau kentang.");
        if (gizi.getTotalKalori() > MAX_KALORI)
            rec.add("Kalori terlalu tinggi: kurangi porsi karbohidrat.");

        if (gizi.getTotalProtein() < MIN_PROTEIN)
            rec.add("Protein kurang: tambahkan ayam, telur, ikan, atau tempe.");

        if (gizi.getTotalLemak() > MAX_LEMAK)
            rec.add("Lemak berlebih: kurangi gorengan dan santan.");

        if (rec.isEmpty())
            rec.add("Menu sudah seimbang. Tidak ada rekomendasi tambahan.");

        return rec;
    }

    // MENU DEFAULT (Misal data 7 hari terakhir ga ada)
    private List<String> generateOptimizedMenu() {
        List<String> menu = new ArrayList<>();

        menu.add("Nasi porsi sedang");
        menu.add("Lauk ayam panggang / tahu / tempe");
        menu.add("Sayur bening atau capcay");
        menu.add("Buah 1 porsi");
        menu.add("Air putih minimal 2 gelas");

        return menu;
    }

    // MEAL PLAN 7 HARI (dari database kalau ada)
    public Map<LocalDate, List<String>> generateWeeklyMealPlan() {

        Map<LocalDate, List<String>> mealPlan = new HashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 1; i <= 7; i++) {
            LocalDate date = today.minusDays(i);

            // skip Minggu
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) continue;

            // ambil menu dari repository (jika ada)
        //    List<String> storedMenu = mealRepo.getMenuForDate(date);

      //      if (storedMenu != null && !storedMenu.isEmpty()) {
                // gunakan data lama
 //               mealPlan.put(date, storedMenu);
     //       } else {
                // tidak ada → generate baru
                mealPlan.put(date, generateOptimizedMenu());
            }
 //       }

        return mealPlan;
    }
}