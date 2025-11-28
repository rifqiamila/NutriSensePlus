package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.layout.Router;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class SidebarSiswaController {

    @FXML private HBox dashboardBtn;
    @FXML private HBox analyzerBtn;
    @FXML private HBox giziBtn;
    @FXML private HBox feedbackBtn;
    @FXML private HBox riwayatBtn;
    @FXML private HBox logoutBtn;

    @FXML
    private void initialize() {
        setupNav(dashboardBtn);
        setupNav(analyzerBtn);
        setupNav(giziBtn);
        setupNav(feedbackBtn);
        setupNav(riwayatBtn);
        setupNav(logoutBtn);
    }

    private void setupNav(HBox box) {
        box.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Router.navigate(box.getAccessibleText());
        });
    }
}