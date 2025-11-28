package com.nutrisense.controllers.main;

import java.io.IOException;

import com.nutrisense.controllers.sidebar.SidebarBaseController;
import com.nutrisense.controllers.user_umum.HomeController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private AnchorPane sidebarContainer;

    @FXML
    private AnchorPane contentContainer;

    private String userRole;

    public void init(String role) {
        this.userRole = role;
        loadSidebar(role);
        loadDefaultPage(role);
    }

    // ------------------ LOAD SIDEBAR ------------------

    private void loadSidebar(String role) {
        String sidebarPath = switch (role.toUpperCase()) {
            case "ADMIN" -> "/fxml/sidebar/sidebar_admin.fxml";
            case "DAPUR" -> "/fxml/sidebar/sidebar_dapur.fxml";
            case "SISWA" -> "/fxml/sidebar/sidebar_siswa.fxml";
            default -> "/fxml/sidebar/sidebar_user_umum.fxml";
        };

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sidebarPath));
            Node sidebar = loader.load();

            // ambil controller sidebarnya
            Object controller = loader.getController();
            if (controller instanceof SidebarBaseController sidebarCtrl) {
                sidebarCtrl.setMainController(this);
            }

            setToAnchorPane(sidebarContainer, sidebar);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------ LOAD DEFAULT PAGE BY ROLE ------------------

    private void loadDefaultPage(String role) {
        String page = switch (role.toUpperCase()) {
            case "ADMIN" -> "/fxml/admin/admin_dashboard.fxml";
            case "DAPUR" -> "/fxml/dapur/dapur_dashboard.fxml";
            case "SISWA" -> "/fxml/siswa/siswa_dashboard.fxml";
            default -> "/fxml/user_umum/home.fxml";
        };

        loadPage(page);
    }

    // ------------------ LOAD PAGE ------------------

    public void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node page = loader.load();
            
            // Inject MainController ke page controller jika needed
            Object controller = loader.getController();
            if (controller instanceof HomeController homeCtrl) {
                homeCtrl.setMainController(this);
            }
            // Tambahkan else if untuk controller lain nanti
            
            setToAnchorPane(contentContainer, page);
        } catch (IOException e) {
            System.out.println("Gagal load halaman: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // ------------------ SET ANCHOR ------------------

    private void setToAnchorPane(AnchorPane root, Node content) {
        root.getChildren().clear();
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        root.getChildren().add(content);
    }
}