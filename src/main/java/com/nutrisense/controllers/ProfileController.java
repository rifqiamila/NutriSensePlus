package com.nutrisense.controllers;

import com.nutrisense.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {
    @FXML private Label nameLabel;
    public void initialize() { nameLabel.setText("User Umum Demo"); }
    @FXML private void onBack(){ SceneSwitcher.switchTo("dashboard"); }
}
