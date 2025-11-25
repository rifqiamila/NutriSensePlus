package com.nutrisense.controllers;

import com.nutrisense.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label titleLabel;

    public void initialize() { titleLabel.setText("NutriSense+ Dashboard (User Umum)"); }

    @FXML private void openAnalyzer() { SceneSwitcher.switchTo("food_analyzer"); }
    @FXML private void openRecommendation() { SceneSwitcher.switchTo("recommendation"); }
    @FXML private void openReport() { SceneSwitcher.switchTo("report"); }
    @FXML private void openProfile() { SceneSwitcher.switchTo("profile"); }
    @FXML private void logout() { SceneSwitcher.switchTo("login"); }
}
