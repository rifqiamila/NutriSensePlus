module com.nutrisense {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    
    opens com.nutrisense to javafx.fxml;
    opens com.nutrisense.controllers to javafx.fxml;
    opens com.nutrisense.controllers.main to javafx.fxml;
    opens com.nutrisense.controllers.sidebar to javafx.fxml;
    opens com.nutrisense.controllers.user_umum to javafx.fxml;
    opens com.nutrisense.models.user to javafx.fxml;
    opens com.nutrisense.models.makanan to javafx.fxml;
    opens com.nutrisense.models.laporan to javafx.fxml;
    opens com.nutrisense.models.report to javafx.fxml;
    
    exports com.nutrisense;
    exports com.nutrisense.controllers;
    exports com.nutrisense.controllers.main;
    exports com.nutrisense.controllers.sidebar;
    exports com.nutrisense.controllers.user_umum;
    exports com.nutrisense.models.user;
    exports com.nutrisense.models.makanan;
    exports com.nutrisense.models.laporan;
    exports com.nutrisense.models.report;
}