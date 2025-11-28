package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.layout.Router;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class SidebarUmumController {

    @FXML private HBox homeBtn;
    @FXML private HBox analyzerBtn;
    @FXML private HBox akgBtn;
    @FXML private HBox listMenuBtn;
    @FXML private HBox loginBtn;

    @FXML
    private void initialize() {
        setupNav(homeBtn);
        setupNav(analyzerBtn);
        setupNav(akgBtn);
        setupNav(listMenuBtn);
        setupNav(loginBtn);
    }

    private void setupNav(HBox box) {
        box.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            String path = box.getAccessibleText();
            Router.navigate(path);
        });
    }
}
