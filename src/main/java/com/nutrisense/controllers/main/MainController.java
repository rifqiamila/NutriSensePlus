package com.nutrisense.controllers.main;

import java.io.IOException;

import com.nutrisense.controllers.user_umum.HomeController;
import com.nutrisense.models.user.User;
import com.nutrisense.models.user.User.UserRole;

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
    private User currentUser;  // 🔥 NEW: Store current logged in user

    // 🔥 NEW: Session management methods
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.userRole = user.getRole().name();
        
        // Reload sidebar and content based on the actual user role
        loadSidebar(this.userRole);
        loadDefaultPage(this.userRole);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getUserRole() {
        return userRole;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
        this.userRole = "UMUM";
        loadSidebar("UMUM");
        loadPage("/fxml/user_umum/home.fxml");
    }

    // 🔥 NEW: Enhanced init method that can handle User object
    public void init(User user) {
        if (user != null) {
            setCurrentUser(user);
        } else {
            init("UMUM");
        }
    }

    // Original init method (kept for compatibility)
    public void init(String role) {
        this.userRole = role;
        this.currentUser = null; // No user logged in
        
        loadSidebar(role);
        loadDefaultPage(role);
    }

    // ------------------ LOAD SIDEBAR ------------------

    private void loadSidebar(String role) {
        String sidebarPath = switch (role.toUpperCase()) {
            case "ADMIN" -> "/fxml/sidebar/sidebar_admin.fxml";
            case "DAPUR_MBG" -> "/fxml/sidebar/sidebar_dapur.fxml";  // 🔥 Updated to match UserRole
            case "SISWA" -> "/fxml/sidebar/sidebar_siswa.fxml";
            default -> "/fxml/sidebar/sidebar_user_umum.fxml";
        };

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sidebarPath));
            Node sidebar = loader.load();

            // Inject MainController to sidebar controller
            Object controller = loader.getController();
            if (controller instanceof SidebarController sidebarCtrl) {
                sidebarCtrl.setMainController(this);
                
                // 🔥 NEW: Also pass current user info if needed
                sidebarCtrl.setCurrentUser(currentUser);
            }

            setToAnchorPane(sidebarContainer, sidebar);

        } catch (IOException e) {
            System.err.println("Error loading sidebar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateContentPadding(int leftPadding) {
    contentContainer.setStyle("-fx-padding: 0 0 0 " + leftPadding + ";");
}

    // ------------------ LOAD DEFAULT PAGE BY ROLE ------------------

    private void loadDefaultPage(String role) {
        String page = switch (role.toUpperCase()) {
            case "ADMIN" -> {
                // Cek dulu file admin dashboard, kalo ga ada fallback ke home
                String adminPath = "/fxml/admin/dashboard.fxml";
                if (getClass().getResource(adminPath) != null) {
                    yield adminPath;
                } else {
                    System.out.println("⚠️ Admin dashboard not found, falling back to home");
                    yield "/fxml/user_umum/home.fxml";
                }
            }
            case "DAPUR_MBG" -> {
                String dapurPath = "/fxml/dapur/dashboard.fxml";
                if (getClass().getResource(dapurPath) != null) {
                    yield dapurPath;
                } else {
                    System.out.println("⚠️ Dapur dashboard not found, falling back to home");
                    yield "/fxml/user_umum/home.fxml";
                }
            }
            case "SISWA" -> {
                String siswaPath = "/fxml/siswa/dashboard.fxml";
                if (getClass().getResource(siswaPath) != null) {
                    yield siswaPath;
                } else {
                    System.out.println("⚠️ Siswa dashboard not found, falling back to home");
                    yield "/fxml/user_umum/home.fxml";
                }
            }
            default -> "/fxml/user_umum/home.fxml";
        };

        loadPage(page);
    }

    // ------------------ LOAD PAGE ------------------

    public void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node page = loader.load();
            
            // 🔥 ENHANCED: Inject MainController to ALL controllers
            Object controller = loader.getController();
            
            if (controller instanceof HomeController homeCtrl) {
                homeCtrl.setMainController(this);
            }
            else if (controller instanceof LoginController loginCtrl) {
                loginCtrl.setMainController(this); // 🔥 INI YANG PERLU DITAMBAH
                System.out.println("🎯 Injected MainController into LoginController");
            }
            else if (controller instanceof com.nutrisense.controllers.siswa.SiswaDashboardController siswaCtrl) {
                siswaCtrl.setMainController(this);
            }
            else if (controller instanceof com.nutrisense.controllers.dapur.DapurDashboardController dapurCtrl) {
                dapurCtrl.setMainController(this);
            }
            else if (controller instanceof com.nutrisense.controllers.admin.AdminDashboardController adminCtrl) {
                adminCtrl.setMainController(this);
            }
            
            setToAnchorPane(contentContainer, page);
            
        } catch (IOException e) {
            System.err.println("Gagal load halaman: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // 🔥 NEW: Helper method to inject current user to controllers
    private void injectCurrentUserToController(Object controller) {
        try {
            // Use reflection to call setCurrentUser if the method exists
            var method = controller.getClass().getMethod("setCurrentUser", User.class);
            method.invoke(controller, currentUser);
        } catch (Exception e) {
            // Method doesn't exist, that's fine
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

    // 🔥 NEW: Utility methods for role-based access control
    public boolean hasRole(UserRole role) {
        return currentUser != null && currentUser.getRole() == role;
    }

    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }

    public boolean isSiswa() {
        return hasRole(UserRole.SISWA);
    }

    public boolean isDapurMBG() {
        return hasRole(UserRole.DAPUR_MBG);
    }

    public boolean isUmum() {
        return currentUser == null || hasRole(UserRole.UMUM);
    }
}