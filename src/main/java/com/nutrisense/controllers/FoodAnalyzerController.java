package com.nutrisense.controllers;

import com.nutrisense.models.GiziResult;
import com.nutrisense.services.FakeGiziService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import com.nutrisense.SceneSwitcher;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodAnalyzerController {
    @FXML private TextField foodField, gramsField;
    @FXML private PieChart pieChart;
    @FXML private TextArea detailsArea;

    private final FakeGiziService gizi = FakeGiziService.getInstance();

    @FXML private void onAnalyze() {
        String name = foodField.getText().trim();
        double grams = 100;
        try { grams = Double.parseDouble(gramsField.getText().trim()); } catch (Exception ignored) {}
        GiziResult r = gizi.analyzeFood(name, grams);
        detailsArea.setText(r.detailedString());
        pieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Kalori", r.getCalories()),
                new PieChart.Data("Protein", r.getProtein()),
                new PieChart.Data("Lemak", r.getFat())
        ));
    }
    @FXML private void onBack() { 
        SceneSwitcher.switchTo("dashboard"); 
    }
}
