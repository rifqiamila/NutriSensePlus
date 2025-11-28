package com.nutrisense.controllers.auth;

import com.nutrisense.controllers.main.MainController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validasi field kosong
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Isi username dan password!");
            return;
        }

        // Dummy login (nanti ganti database)
        String role = authenticateUser(username, password);

        if (role == null) {
            errorLabel.setText("Username atau password salah!");
            return;
        }

        openMainLayout(role);
    }

    /** Dummy authentication, nanti hubungkan JSON/DB */
    private String authenticateUser(String user, String pass) {
        if (user.equals("admin") && pass.equals("123")) return "ADMIN";
        if (user.equals("dapur") && pass.equals("123")) return "DAPUR";
        if (user.equals("siswa") && pass.equals("123")) return "SISWA";

        return null; // salah → bukan fallback UMUM
    }

    /** Load MainLayout dan kirim role ke MainController */
    private void openMainLayout(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.init(role);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearError() {
        errorLabel.setText("");
    }
}
