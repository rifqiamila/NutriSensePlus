package com.nutrisense;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent; import javafx.scene.Scene; import javafx.stage.Stage;
public class SceneSwitcher {

    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchTo(String fxmlName) {
        try {
            Parent root = FXMLLoader.load(
                SceneSwitcher.class.getResource("/fxml/" + fxmlName + ".fxml")
            );
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
