package com.nutrisense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneSwitcher.setStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/nutrisense.css").toExternalForm());
        stage.setTitle("NutriSense+");
        stage.setScene(scene);

        // Size the window relative to the primary screen's usable area so it fits laptop screens
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double width = visualBounds.getWidth() * 0.90; // 90% of available width
        double height = visualBounds.getHeight() * 0.90; // 90% of available height
        // Fallback to sensible defaults in case visualBounds is unexpected
        if (Double.isNaN(width) || width <= 0) width = 1200;
        if (Double.isNaN(height) || height <= 0) height = 800;

        stage.setWidth(width);
        stage.setHeight(height);
        // center the stage in the visual bounds
        stage.setX(visualBounds.getMinX() + (visualBounds.getWidth() - width) / 2);
        stage.setY(visualBounds.getMinY() + (visualBounds.getHeight() - height) / 2);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
