// package com.nutrisense.controllers;

// import com.nutrisense.SceneSwitcher;
// import com.nutrisense.services.FakeRecommendationService;
// import javafx.collections.FXCollections;
// import javafx.fxml.FXML;
// import javafx.scene.control.ListView;
// import javafx.scene.control.TextField;

// public class RecommendationController {
//     @FXML private TextField queryField;
//     @FXML private ListView<String> listView;

//     private final FakeRecommendationService rec = FakeRecommendationService.getInstance();

//     @FXML private void onGenerate() {
//         String q = queryField.getText().trim();
//         listView.setItems(FXCollections.observableArrayList(rec.getRecommendation(q)));
//     }

//     @FXML private void onBack() { 
//         SceneSwitcher.switchTo("dashboard"); 
//     }
// }
