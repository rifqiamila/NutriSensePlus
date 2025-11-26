module com.nutrisense {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.nutrisense to javafx.fxml;
    opens com.nutrisense.controllers to javafx.fxml;
    opens com.nutrisense.models to com.google.gson;
    opens com.nutrisense.utils to com.google.gson;

    exports com.nutrisense;
}
