package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.main.SidebarController;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SidebarUserUmumController extends SidebarController {

    @FXML
    private VBox sidebarPane;
    private boolean isExpanded = false;

    @Override
    protected void updateSidebarForUser() {
        System.out.println("👤 Sidebar updated for user umum");
    }

    @FXML
    private void handleToggleClick() {
        if (isExpanded) {
            sidebarPane.setPrefWidth(70);
            sidebarPane.getStyleClass().remove("expanded");
        } else {
            sidebarPane.setPrefWidth(200);
            sidebarPane.getStyleClass().add("expanded");
        }
        isExpanded = !isExpanded;
    }

    // 🔥 COPY METHOD DARI PARENT (kalo ga bisa access parent)
    @FXML
    private void onMenuClicked(MouseEvent event) {
        System.out.println("🎯 MENU CLICKED - Child class method");
        HBox item = (HBox) event.getSource();
        String target = item.getAccessibleText();

        if (target == null || target.isEmpty()) return;

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
}