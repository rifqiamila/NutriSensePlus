package com.nutrisense.services;

import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.report.IndicatorStatus;

public class GiziBalanceTracker {
    public IndicatorStatus evaluate(GiziResult gizi) {

        double kalori = gizi.getTotalKalori();
        double protein = gizi.getTotalProtein();
        double lemak = gizi.getTotalLemak();

        // MERAH
        if (kalori < 500 || protein < 15 || lemak > 30) {
            return IndicatorStatus.MERAH;
        }

        // KUNING
        if (kalori < 600 || protein < 23 || lemak > 25) {
            return IndicatorStatus.KUNING;
        }

        // HIJAU
        return IndicatorStatus.HIJAU;
    }
}
