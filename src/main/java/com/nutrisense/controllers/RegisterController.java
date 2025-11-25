package com.nutrisense.controllers;

import com.nutrisense.SceneSwitcher;
import com.nutrisense.services.FakeAuthService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {
    @FXML private ComboBox<String> roleBox;
    @FXML private TextField f1, f2, f3;
    @FXML private PasswordField pw;
    @FXML private Label status;

    private final FakeAuthService auth = FakeAuthService.getInstance();

    public void initialize() {
        roleBox.getItems().addAll("Siswa", "Dapur MBG");
        roleBox.setValue("Siswa");
    }

    @FXML private void onRegister() {
        if (f1.getText().isEmpty() || pw.getText().isEmpty()) { status.setText("Lengkapi data"); return; }
        auth.register(f1.getText(), pw.getText(), roleBox.getValue());
        status.setText("Terdaftar. Kembali ke login");
    }

    @FXML private void onBack() { SceneSwitcher.switchTo("login");
; }
}
