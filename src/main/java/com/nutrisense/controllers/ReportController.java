package com.nutrisense.controllers;

import com.nutrisense.services.FakeRecommendationService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ReportController {
    @FXML private TextArea reportArea;
    private final FakeRecommendationService svc = FakeRecommendationService.getInstance();

    public void initialize() {
        reportArea.setText("Laporan contoh:\n" + String.join("\n", svc.getRecommendation("")));
    }
    @FXML private void onBack() { com.nutrisense.SceneSwitcher.switchTo("dashboard"); }
}
