package com.nutrisense.controllers.layout;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class Router {

    private static BorderPane mainLayout;

    public static void setMainLayout(BorderPane layout) {
        mainLayout = layout;
    }

    public static void navigate(String fxmlPath) {
        try {
            Node content = FXMLLoader.load(Router.class.getResource(fxmlPath));
            mainLayout.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load page: " + fxmlPath);
        }
    }
}