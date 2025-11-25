module com.nutrisense {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.nutrisense to javafx.fxml;
    opens com.nutrisense.controllers to javafx.fxml;

    exports com.nutrisense;
}
