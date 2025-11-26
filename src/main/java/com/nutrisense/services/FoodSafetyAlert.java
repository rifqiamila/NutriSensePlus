package com.nutrisense.services;

import com.nutrisense.models.report.IndicatorStatus;

public class FoodSafetyAlert {

    public boolean isAllowedToServe(IndicatorStatus status) {
        return status != IndicatorStatus.MERAH;
    }

    public String getWarningMessage(IndicatorStatus status) {
        switch (status) {
            case MERAH:
                return "⚠ Status MERAH: Menu tidak boleh disajikan!";
            case KUNING:
                return "⚠ Status KUNING: Gizi kurang, perlu perbaikan.";
            case HIJAU:
            default:
                return "✔ Gizi seimbang.";
        }
    }
}
