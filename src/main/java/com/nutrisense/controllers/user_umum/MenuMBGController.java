package com.nutrisense.controllers.user_umum;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutrisense.controllers.main.MainController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MenuMBGController implements Initializable {

    @FXML private TextField searchField;
    @FXML private VBox dapurListContainer;
    @FXML private VBox placeholder;

    private MainController mainController;
    private List<DapurMenu> daftarDapur = new ArrayList<>();
    private final Gson gson = new Gson();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMenuData();
        setupSearch();
    }

    private void loadMenuData() {
        try (InputStream is = getClass().getResourceAsStream("/data/menu_mbg.json");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            daftarDapur = gson.fromJson(reader, new TypeToken<List<DapurMenu>>(){}.getType());
            Platform.runLater(this::displayAllDapur);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal memuat data menu MBG");
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            String query = newVal == null ? "" : newVal.toLowerCase().trim();
            if (query.isEmpty()) {
                displayAllDapur();
            } else {
                List<DapurMenu> filtered = daftarDapur.stream()
                    .filter(d -> d.namaDapur.toLowerCase().contains(query) ||
                                 d.sekolah.toLowerCase().contains(query))
                    .collect(Collectors.toList());
                displayDapurList(filtered);
            }
        });
    }

    private void displayAllDapur() {
        displayDapurList(daftarDapur);
    }

    private void displayDapurList(List<DapurMenu> list) {
        dapurListContainer.getChildren().clear();
        if (list.isEmpty()) {
            Label noResult = new Label("Tidak ditemukan dapur dengan kata kunci tersebut");
            noResult.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
            dapurListContainer.getChildren().add(noResult);
            return;
        }

        for (DapurMenu dapur : list) {
            VBox card = createDapurCard(dapur);
            dapurListContainer.getChildren().add(card);
        }
    }

    private VBox createDapurCard(DapurMenu dapur) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");
        card.setOnMouseClicked(e -> showMenu7Hari(dapur));

        Label nama = new Label(dapur.namaDapur);
        nama.setFont(Font.font("System", 16));
        nama.setStyle("-fx-font-weight: bold;");

        Label sekolah = new Label(dapur.sekolah);
        sekolah.setStyle("-fx-text-fill: #7f8c8d;");

        Label count = new Label(dapur.menu7Hari.size() + " hari menu tersedia");
        count.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        card.getChildren().addAll(nama, sekolah, count);
        return card;
    }

    private void showMenu7Hari(DapurMenu dapur) {
        placeholder.setVisible(false);

        VBox detail = new VBox(20);
        detail.setPadding(new Insets(20));
        detail.setStyle("-fx-background-color: #f0f7ff; -fx-background-radius: 12;");

        Label title = new Label("Menu 7 Hari - " + dapur.namaDapur);
        title.setFont(Font.font(20));
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox menuBox = new VBox(14);
        for (var hari : dapur.menu7Hari) {
            VBox hariBox = new VBox(8);
            hariBox.setPadding(new Insets(16));
            hariBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

            // 1. HARI & TANGGAL (dideklarasikan DULU!)
            Label hariLabel = new Label(hari.hari + " (" + hari.tanggal + ")");
            hariLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 15px;");

            // 2. GIZI BARIS PERTAMA
            Label giziLabel = new Label(
                "Kalori: " + (hari.gizi != null ? hari.gizi.kalori : 0) + " kcal • " +
                "Protein: " + (hari.gizi != null ? hari.gizi.protein : 0) + "g • " +
                "Lemak: " + (hari.gizi != null ? hari.gizi.lemak : 0) + "g"
            );
            giziLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold; -fx-font-size: 13px;");

            // 3. GIZI BARIS KEDUA
            Label giziLabel2 = new Label(
                "Karbohidrat: " + (hari.gizi != null ? hari.gizi.karbohidrat : 0) + "g • " +
                "Serat: " + (hari.gizi != null ? hari.gizi.serat : 0) + "g"
            );
            giziLabel2.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 13px;");

            // 4. LIST MENU (dideklarasikan DULU sebelum dipakai!)
            VBox items = new VBox(4);
            items.setPadding(new Insets(4, 0, 0, 12));
            for (String makanan : hari.menu) {
                Label item = new Label("• " + makanan);
                item.setStyle("-fx-text-fill: #34495e;");
                items.getChildren().add(item);
            }

            // Sekarang baru tambahkan ke hariBox (urutan sudah benar!)
            hariBox.getChildren().addAll(hariLabel, giziLabel, giziLabel2, items);
            menuBox.getChildren().add(hariBox);
        }

        detail.getChildren().addAll(title, menuBox);
        dapurListContainer.getChildren().clear();
        dapurListContainer.getChildren().add(detail);
    }

    @FXML
    private void onBack() {
        if (mainController != null) {
            mainController.loadPage("/fxml/user_umum/home.fxml");
        }
    }

    private void showError(String msg) {
        Label err = new Label("Error: " + msg);
        err.setTextFill(Color.RED);
        dapurListContainer.getChildren().add(err);
    }

    // === INNER CLASS MODEL ===
    public static class DapurMenu {
        public String dapurId;
        public String namaDapur;
        public String sekolah;
        public List<HariMenu> menu7Hari;
    }

    public static class HariMenu {
        public String tanggal;
        public String hari;
        public List<String> menu;

        public Gizi gizi;

        public HariMenu(){}
    }

    public static class Gizi {
        public int kalori;
        public double protein;
        public double lemak;
        public double karbohidrat;
        public double serat;

        public Gizi() {} 
    }
}