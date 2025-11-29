package com.nutrisense.controllers.main;

import com.nutrisense.models.user.User;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public abstract class SidebarController {

    protected MainController mainController;  // 🔥 REMOVE static
    protected User currentUser;

    /** Dipanggil dari MainController setelah load sidebar */
    public void setMainController(MainController controller) {  // 🔥 REMOVE static
        this.mainController = controller;
    }

    // Method to receive current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateSidebarForUser();
    }

    protected abstract void updateSidebarForUser();

    @FXML
    private void onSidebarItemClicked(MouseEvent event) {
        HBox item = (HBox) event.getSource();
        String target = item.getAccessibleText();

        if (target == null || target.isEmpty()) return;

        if (mainController != null) {
            mainController.loadPage(target);
        }
    }
    
    // 🔥 NEW: Common logout method
    @FXML
    protected void onLogout() {
        if (mainController != null) {
            mainController.logout();
        }
    }
}