package com.nutrisense.controllers;

import com.nutrisense.SceneSwitcher;
import com.nutrisense.services.FakeAuthService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML private ComboBox<String> roleBox;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final FakeAuthService auth = FakeAuthService.getInstance();

    public void initialize() {
        roleBox.getItems().addAll("User Umum", "Siswa", "Dapur MBG");
        roleBox.setValue("User Umum");
    }

    @FXML private void onLogin() {
        String role = roleBox.getValue();
        String u = userField.getText().trim();
        String p = passwordField.getText().trim();

        // For demo we auto-allow "User Umum" to enter dashboard
        if ("User Umum".equals(role)) {
            SceneSwitcher.switchTo("dashboard");
            return;
        }

        boolean ok = "Siswa".equals(role) ? auth.loginStudent(u, p) :auth.loginKitchen(u, p);
        if (ok) SceneSwitcher.switchTo("dashboard");
        else statusLabel.setText("Login gagal — contoh: user / 1234");
    }

    @FXML private void onRegister() { SceneSwitcher.switchTo("register"); }
}
