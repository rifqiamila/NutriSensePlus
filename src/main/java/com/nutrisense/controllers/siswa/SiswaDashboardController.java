package com.nutrisense.controllers.siswa;

import com.nutrisense.controllers.main.MainController;
import com.nutrisense.models.user.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SiswaDashboardController {
    
    private MainController mainController;
    private User currentUser;
    
    @FXML private Label welcomeLabel;
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUI();
    }
    
    private void updateUI() {
        if (currentUser != null) {
            welcomeLabel.setText("Halo, " + currentUser.getUsername());
        }
    }
    
    @FXML
    private void initialize() {
        // Initialization code
    }
}