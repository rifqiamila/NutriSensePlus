package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.main.SidebarController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class SidebarUserUmumController extends SidebarController {

    @FXML
    private VBox sidebarPane;
    @FXML
    private Label lblLogo;
    
    // 🔥 TAMBAHKAN DEKLARASI VARIABEL MENU DI SINI
    @FXML
    private HBox menuHome;
    @FXML
    private HBox menuAnalyzer;
    @FXML
    private HBox menuAKG;
    @FXML
    private HBox menuListMBG;
    @FXML
    private HBox menuLogin;
    
    private boolean isExpanded = false;

    @Override
    protected void updateSidebarForUser() {
        System.out.println("👤 Sidebar updated for user umum");
    }

    @FXML
    private void handleToggleClick() {
        if (isExpanded) {
            // Collapse sidebar
            sidebarPane.setPrefWidth(70);
            sidebarPane.getStyleClass().remove("expanded");
            lblLogo.setVisible(false);
        } else {
            // Expand sidebar
            sidebarPane.setPrefWidth(200);
            sidebarPane.getStyleClass().add("expanded");
            lblLogo.setVisible(true);
        }
        isExpanded = !isExpanded;
    }

    @FXML
    private void onMenuClicked(MouseEvent event) {
        System.out.println("🎯 MENU CLICKED - Child class method");
        HBox item = (HBox) event.getSource();
        String target = item.getAccessibleText();

        if (target == null || target.isEmpty()) return;

        // Remove active state from all items
        removeActiveState();
        
        // Add active state to clicked item
        item.getStyleClass().add("active");

        if (mainController != null) {
            System.out.println("🚀 Loading: " + target);
            mainController.loadPage(target);
        } else {
            System.out.println("❌ mainController is NULL in child class!");
        }
    }

    private void removeActiveState() {
        sidebarPane.lookupAll(".rail-item").forEach(node -> 
            node.getStyleClass().remove("active")
        );
    }

    // Method untuk set active menu berdasarkan halaman saat ini
    public void setActiveMenu(String menuId) {
        removeActiveState();
        
        switch(menuId) {
            case "home":
                menuHome.getStyleClass().add("active");
                break;
            case "analyzer":
                menuAnalyzer.getStyleClass().add("active");
                break;
            case "akg":
                menuAKG.getStyleClass().add("active");
                break;
            case "mbg":
                menuListMBG.getStyleClass().add("active");
                break;
            case "login":
                menuLogin.getStyleClass().add("active");
                break;
        }
    }
}