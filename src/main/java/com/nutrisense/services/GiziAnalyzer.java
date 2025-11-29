package com.nutrisense.services;

import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.MenuMBG;

public class GiziAnalyzer {
    public GiziResult analyzeMenu(MenuMBG menu) {
        return menu.getTotalGizi();
    }
}
