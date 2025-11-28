package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.layout.Router;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class SidebarDapurController {

    @FXML private HBox dashboardBtn;
    @FXML private HBox menuTodayBtn;
    @FXML private HBox menuGenerateBtn;
    @FXML private HBox menuEditBtn;
    @FXML private HBox foodSafetyBtn;
    @FXML private HBox reportBtn;
    @FXML private HBox feedbackBtn;
    @FXML private HBox dataSiswaBtn;
    @FXML private HBox logoutBtn;

    @FXML
    private void initialize() {
        setupNav(dashboardBtn);
        setupNav(menuTodayBtn);
        setupNav(menuGenerateBtn);
        setupNav(menuEditBtn);
        setupNav(foodSafetyBtn);
        setupNav(reportBtn);
        setupNav(feedbackBtn);
        setupNav(dataSiswaBtn);
        setupNav(logoutBtn);
    }

    private void setupNav(HBox box) {
        box.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Router.navigate(box.getAccessibleText()));
    }
}