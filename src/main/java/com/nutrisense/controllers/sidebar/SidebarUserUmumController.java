package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.main.MainController;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SidebarUserUmumController implements SidebarBaseController {

    private MainController main;

    @Override
    public void setMainController(MainController controller) {
        this.main = controller;
    }

    @FXML
    private VBox sidebarPane;

    private boolean isExpanded = false;

    @FXML
    private void handleToggleClick() {
        if (isExpanded) {
            // Collapse sidebar
            sidebarPane.setPrefWidth(70);
            sidebarPane.getStyleClass().remove("expanded");
        } else {
            // Expand sidebar  
            sidebarPane.setPrefWidth(200);
            sidebarPane.getStyleClass().add("expanded");
        }
        isExpanded = !isExpanded;
    }

    @FXML
    private void onMenuClicked(MouseEvent event) {
        javafx.scene.layout.HBox hbox = (javafx.scene.layout.HBox) event.getSource();
        String fxmlPath = hbox.getAccessibleText();

        if (main != null && fxmlPath != null) {
            main.loadPage(fxmlPath);
            
            // Optional: Add active state styling
            removeActiveState();
            hbox.getStyleClass().add("active");
        }
    }

    private void removeActiveState() {
        // Remove active class from all menu items
        sidebarPane.lookupAll(".rail-item").forEach(node -> 
            node.getStyleClass().remove("active")
        );
    }
}