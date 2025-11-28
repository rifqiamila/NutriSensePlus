package com.nutrisense.controllers.main;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class SidebarController {

    private static MainController mainController;  

    /** Dipanggil dari MainController setelah load sidebar */
    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    @FXML
    private void onSidebarItemClicked(MouseEvent event) {
        HBox item = (HBox) event.getSource();
        String target = item.getAccessibleText();

        if (target == null || target.isEmpty()) return;

        mainController.loadPage(target);
    }
}
