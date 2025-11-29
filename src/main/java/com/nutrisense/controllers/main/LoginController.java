package com.nutrisense.controllers.main;

import com.nutrisense.controllers.main.MainController;
import com.nutrisense.models.user.User;
import com.nutrisense.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginController {
    
    private MainController mainController;
    private UserService userService;
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    @FXML private VBox loadingIndicator;
    
    public LoginController() {
        this.userService = new UserService();
    }
    
    @FXML
    public void initialize() {
        System.out.println("🔧 LoginController initialized");
        // Setup enter key to trigger login
        passwordField.setOnAction(e -> handleLogin());
        
        // Clear error when typing
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        System.out.println("🎯 MainController set: " + (mainController != null));
        // 🔥 UserService udah di-init di constructor, jadi ga perlu init lagi di sini
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        System.out.println("🔐 Attempting login: " + username); // 🔥 DEBUG
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password harus diisi");
            return;
        }
        
        showLoading(true);
        
        // Run login in background thread
        new Thread(() -> {
            try {
                User user = userService.login(username, password);
                System.out.println("📊 Login result: " + (user != null ? user.getRole() : "NULL")); // 🔥 DEBUG
                
                javafx.application.Platform.runLater(() -> {
                    showLoading(false);
                    
                    if (user != null) {
                        loginSuccess(user);
                    } else {
                        showError("Login gagal. Periksa username dan password");
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace(); // 🔥 DEBUG - print full stack trace
                javafx.application.Platform.runLater(() -> {
                    showLoading(false);
                    showError("Terjadi error: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private void loginSuccess(User user) {
        System.out.println("Login successful: " + user.getUsername() + " (" + user.getRole() + ")");
        
        // Store user session in MainController
        mainController.setCurrentUser(user);
        
        // Redirect based on role
        switch (user.getRole()) {
            case ADMIN:
                mainController.loadPage("/fxml/admin/dashboard.fxml");
                break;
            case SISWA:
                mainController.loadPage("/fxml/siswa/dashboard.fxml");
                break;
            case DAPUR_MBG:
                mainController.loadPage("/fxml/dapur/dashboard.fxml");
                break;
            case UMUM:
            default:
                mainController.loadPage("/fxml/user_umum/home.fxml");
                break;
        }
    }
    
    @FXML
    private void handleBackToHome() {
        mainController.loadPage("/fxml/user_umum/home.fxml");
    }
    
    @FXML
    private void handleRegisterAsSiswa() {
        // Optional: Show registration form for siswa
        // mainController.loadPage("/fxml/register/siswa.fxml");
        showInfo("Gunakan format: nisn_20241001001 untuk auto-register siswa");
    }
    
    @FXML
    private void handleRegisterAsDapur() {
        // Optional: Show registration form for dapur  
        // mainController.loadPage("/fxml/register/dapur.fxml");
        showInfo("Gunakan format: dapur_sd05 untuk auto-register dapur");
    }
    
    private void showLoading(boolean show) {
        loadingIndicator.setVisible(show);
        loginButton.setDisable(show);
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #e74c3c;");
        errorLabel.setVisible(true);
    }
    
    private void showInfo(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #3498db;");
        errorLabel.setVisible(true);
    }
    
    private void clearError() {
        errorLabel.setVisible(false);
    }
}