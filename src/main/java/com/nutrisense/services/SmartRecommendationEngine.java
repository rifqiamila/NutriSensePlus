package com.nutrisense.services;

import java.util.ArrayList;
import java.util.List;

import com.nutrisense.models.makanan.GiziResult;

public class SmartRecommendationEngine {

    public List<String> recommend(GiziResult gizi) {
        List<String> rec = new ArrayList<>();

        if (gizi.getTotalKalori() < 600)
            rec.add("Tambahkan nasi, roti, atau kentang untuk meningkatkan energi.");

        if (gizi.getTotalProtein() < 23)
            rec.add("Tambahkan ayam, telur, tahu atau tempe.");

        if (gizi.getTotalLemak() > 25)
            rec.add("Kurangi makanan berminyak, gorengan, dan santan.");

        if (rec.isEmpty())
            rec.add("Menu sudah baik! Tidak ada rekomendasi tambahan.");

        return rec;
    }
}
