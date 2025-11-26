package com.nutrisense.services;

import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.Makanan;
import com.nutrisense.models.makanan.MenuMBG;

public class GiziAnalyzer {
    public GiziResult analyzeMenu(MenuMBG menu) {
        double totalKalori = 0;
        double totalProtein = 0;
        double totalLemak = 0;
        double totalKarbo = 0;
        double totalSerat = 0;
        for (Makanan makanan : menu.getDaftarMakanan()) {
            totalKalori += makanan.getKalori();
            totalProtein += makanan.getProtein();
            totalLemak += makanan.getLemak();
            totalKarbo += makanan.getKarbohidrat();
            totalSerat += makanan.getSerat();
        }

        return new GiziResult(
                totalKalori,
                totalProtein,
                totalLemak,
                totalKarbo,
                totalSerat
        );
    }
}
