package com.nutrisense.controllers.user_umum;

import com.nutrisense.controllers.main.MainController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class HomeController {

    private MainController mainController;

    // Method ini akan dipanggil dari MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void onTryAnalyzer() {
        if (mainController != null) {
            mainController.loadPage("/fxml/food_analyzer.fxml");
        }
    }

    @FXML
    private void onSeeMenuMBG() {
        if (mainController != null) {
            mainController.loadPage("/fxml/umum_list_menu.fxml");
        }
    }

    @FXML
    private void onLearnMore() {
        // Bisa navigasi ke about page atau buka dialog
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Tentang NutriSense+");
        info.setHeaderText("NutriSense+ - Smart Nutrition Monitoring");
        info.setContentText("Aplikasi cerdas untuk memantau gizi dan keamanan makanan di dapur sekolah.\n\n" +
                           "Dikembangkan dengan JavaFX untuk menyelesaikan tantangan analisis gizi manual.");
        info.showAndWait();
    }

    @FXML
    private void onStartNow() {
        onTryAnalyzer();
    }
}