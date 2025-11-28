package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.main.MainController;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class SidebarDapurController implements SidebarBaseController {

    private MainController main;

    @Override
    public void setMainController(MainController controller) {
        this.main = controller;
    }

    @FXML
    private void homeClicked() {
        main.loadPage("/fxml/dapur/dapur_dashboard.fxml");
    }

    @FXML
    private void onMenuClicked(MouseEvent event) {
        // Ambil HBox yang diklik
        javafx.scene.layout.HBox hbox = (javafx.scene.layout.HBox) event.getSource();
        String fxmlPath = hbox.getAccessibleText();

        if (main != null && fxmlPath != null) {
            main.loadPage(fxmlPath);
        }
    }
}