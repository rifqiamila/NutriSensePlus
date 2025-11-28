package com.nutrisense.controllers.sidebar;

import com.nutrisense.controllers.layout.Router;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class SidebarAdminController {  // Superadmin

    @FXML private HBox dashboardBtn;
    @FXML private HBox requestBtn;
    @FXML private HBox siswaBtn;
    @FXML private HBox dapurBtn;
    @FXML private HBox logoutBtn;

    @FXML
    private void initialize() {
        setupNav(dashboardBtn);
        setupNav(requestBtn);
        setupNav(siswaBtn);
        setupNav(dapurBtn);
        setupNav(logoutBtn);
    }

    private void setupNav(HBox box) {
        box.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
                Router.navigate(box.getAccessibleText())
        );
    }
}
