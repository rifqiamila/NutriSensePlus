package com.nutrisense.controllers.admin;

import com.nutrisense.controllers.main.MainController;
import com.nutrisense.models.user.User;

public class AdminDashboardController {
    
    private MainController mainController;
    private User currentUser;
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}