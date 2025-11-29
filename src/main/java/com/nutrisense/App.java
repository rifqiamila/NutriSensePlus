package com.nutrisense;

import com.nutrisense.controllers.main.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneSwitcher.setStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/MainLayout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        MainController controller = loader.getController();

        scene.getStylesheets().add(getClass().getResource("/css/nutrisense.css").toExternalForm());
        stage.setTitle("NutriSense+");
        stage.setScene(scene);

        // Size the window
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double width = visualBounds.getWidth() * 0.90;
        double height = visualBounds.getHeight() * 0.90;
        if (Double.isNaN(width) || width <= 0) width = 1200;
        if (Double.isNaN(height) || height <= 0) height = 800;

        stage.setWidth(width);
        stage.setHeight(height);
        stage.setX(visualBounds.getMinX() + (visualBounds.getWidth() - width) / 2);
        stage.setY(visualBounds.getMinY() + (visualBounds.getHeight() - height) / 2);

        stage.show();

        // 🔥 FIX: Panggil init() SETELAH window show
        // Ini memastikan semua FXML components udah fully loaded
        controller.init("umum");
    }

    public static void main(String[] args) {
        launch();
    }
}