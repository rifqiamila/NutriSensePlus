package com.nutrisense.controllers.user_umum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nutrisense.controllers.main.MainController;
import com.nutrisense.models.makanan.MenuMBG;
import com.nutrisense.models.makanan.MenuMakanan;
import com.nutrisense.models.makanan.Makanan;
import com.nutrisense.models.user.Sekolah;
import com.nutrisense.repositories.SekolahRepository;
import com.nutrisense.utils.LocalDateAdapter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MenuMBGController implements Initializable {

    @FXML private TextField searchField;
    @FXML private VBox dapurListContainer;
    @FXML private VBox placeholder;

    private MainController mainController;
    private List<MenuMBG> semuaMenu = new ArrayList<>();
    private Map<String, String> sekolahMap = new HashMap<>();
    private final Gson gson = createGson();
    private SekolahRepository sekolahRepository;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Custom Gson builder untuk handle LocalDate
    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sekolahRepository = new SekolahRepository();
        loadSekolahData();
        loadMenuData();
        setupSearch();
    }

    private void loadSekolahData() {
        try {
            List<Sekolah> semuaSekolah = sekolahRepository.findAll();
            for (Sekolah sekolah : semuaSekolah) {
                sekolahMap.put(sekolah.getId(), sekolah.getNama());
            }
            System.out.println("✅ Loaded " + sekolahMap.size() + " schools");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal memuat data sekolah");
        }
    }

    private void loadMenuData() {
        try (InputStream is = getClass().getResourceAsStream("/data/menus.json");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            semuaMenu = gson.fromJson(reader, new TypeToken<List<MenuMBG>>(){}.getType());
            System.out.println("✅ Loaded " + semuaMenu.size() + " menus");
            Platform.runLater(this::displayAllDapur);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal memuat data menu MBG: " + e.getMessage());
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            String query = newVal == null ? "" : newVal.toLowerCase().trim();
            if (query.isEmpty()) {
                displayAllDapur();
            } else {
                List<MenuMBG> filtered = semuaMenu.stream()
                    .filter(menu -> {
                        String namaSekolah = sekolahMap.getOrDefault(menu.getSekolahId(), "").toLowerCase();
                        String dapurId = menu.getDapurId() != null ? menu.getDapurId().toLowerCase() : "";
                        String namaMenu = menu.getNamaMenu() != null ? menu.getNamaMenu().toLowerCase() : "";
                        
                        return namaSekolah.contains(query) || 
                               dapurId.contains(query) || 
                               namaMenu.contains(query);
                    })
                    .collect(Collectors.toList());
                displayDapurList(filtered);
            }
        });
    }

    private void displayAllDapur() {
        displayDapurList(semuaMenu);
    }

    private void displayDapurList(List<MenuMBG> menus) {
        dapurListContainer.getChildren().clear();
        
        if (menus.isEmpty()) {
            Label noResult = new Label("Tidak ditemukan menu dengan kata kunci tersebut");
            noResult.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
            dapurListContainer.getChildren().add(noResult);
            return;
        }

        // Group by dapurId
        Map<String, List<MenuMBG>> menusByDapur = menus.stream()
            .filter(menu -> menu.getDapurId() != null)
            .collect(Collectors.groupingBy(MenuMBG::getDapurId));

        for (Map.Entry<String, List<MenuMBG>> entry : menusByDapur.entrySet()) {
            String dapurId = entry.getKey();
            List<MenuMBG> menusDapur = entry.getValue();
            
            VBox card = createDapurCard(dapurId, menusDapur);
            dapurListContainer.getChildren().add(card);
        }
    }

    private VBox createDapurCard(String dapurId, List<MenuMBG> menusDapur) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");
        card.setOnMouseClicked(e -> showMenuDetail(dapurId, menusDapur));

        // Get sekolah name from first menu
        String sekolahId = menusDapur.get(0).getSekolahId();
        String namaSekolah = sekolahMap.getOrDefault(sekolahId, "Sekolah Tidak Diketahui");

        Label namaDapur = new Label("Dapur: " + dapurId);
        namaDapur.setFont(Font.font("System", 16));
        namaDapur.setStyle("-fx-font-weight: bold;");

        Label sekolah = new Label(namaSekolah);
        sekolah.setStyle("-fx-text-fill: #7f8c8d;");

        // Count unique dates and check if published
        long hariCount = menusDapur.stream()
            .filter(MenuMBG::isPublished)
            .map(MenuMBG::getTanggal)
            .distinct()
            .count();

        Label count = new Label(hariCount + " hari menu tersedia");
        count.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        // Show published status
        Label status = new Label(menusDapur.stream().anyMatch(MenuMBG::isPublished) ? "✅ Published" : "⏳ Draft");
        status.setStyle("-fx-text-fill: " + (menusDapur.stream().anyMatch(MenuMBG::isPublished) ? "#27ae60" : "#f39c12") + ";");

        card.getChildren().addAll(namaDapur, sekolah, count, status);
        return card;
    }

    private void showMenuDetail(String dapurId, List<MenuMBG> menusDapur) {
        placeholder.setVisible(false);

        VBox detail = new VBox(20);
        detail.setPadding(new Insets(20));
        detail.setStyle("-fx-background-color: #f0f7ff; -fx-background-radius: 12;");

        String sekolahId = menusDapur.get(0).getSekolahId();
        String namaSekolah = sekolahMap.getOrDefault(sekolahId, "Sekolah Tidak Diketahui");

        Label title = new Label("Menu - " + dapurId + " (" + namaSekolah + ")");
        title.setFont(Font.font(20));
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox menuBox = new VBox(14);
        
        // Filter only published menus and sort by date
        List<MenuMBG> publishedMenus = menusDapur.stream()
            .filter(MenuMBG::isPublished)
            .sorted(Comparator.comparing(MenuMBG::getTanggal))
            .collect(Collectors.toList());

        if (publishedMenus.isEmpty()) {
            Label noMenu = new Label("Tidak ada menu yang dipublikasikan untuk dapur ini");
            noMenu.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-font-weight: bold;");
            menuBox.getChildren().add(noMenu);
        } else {
            for (MenuMBG menu : publishedMenus) {
                VBox hariBox = new VBox(8);
                hariBox.setPadding(new Insets(16));
                hariBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

                // Menu name and date
                Label menuLabel = new Label(menu.getNamaMenu() != null ? menu.getNamaMenu() : "Menu Harian");
                menuLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 16px;");

                Label tanggalLabel = new Label(menu.getTanggal().format(dateFormatter));
                tanggalLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

                // Total Gizi
                if (menu.getTotalGizi() != null) {
                    var gizi = menu.getTotalGizi();
                    Label giziLabel1 = new Label(
                        "Kalori: " + String.format("%.1f", gizi.getTotalKalori()) + " kcal • " +
                        "Protein: " + String.format("%.1f", gizi.getTotalProtein()) + "g • " + 
                        "Lemak: " + String.format("%.1f", gizi.getTotalLemak()) + "g"
                    );
                    giziLabel1.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold; -fx-font-size: 13px;");

                    Label giziLabel2 = new Label(
                        "Karbohidrat: " + String.format("%.1f", gizi.getTotalKarbohidrat()) + "g • " +
                        "Serat: " + String.format("%.1f", gizi.getTotalSerat()) + "g"
                    );
                    giziLabel2.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 13px;");

                    hariBox.getChildren().addAll(menuLabel, tanggalLabel, giziLabel1, giziLabel2);
                } else {
                    hariBox.getChildren().addAll(menuLabel, tanggalLabel);
                }

                // Food Items
                VBox itemsBox = new VBox(4);
                itemsBox.setPadding(new Insets(8, 0, 0, 12));
                
                if (menu.getItems() != null && !menu.getItems().isEmpty()) {
                    Label itemsTitle = new Label("Makanan:");
                    itemsTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e; -fx-font-size: 14px;");
                    itemsBox.getChildren().add(itemsTitle);
                    
                    for (MenuMakanan item : menu.getItems()) {
                        Makanan makanan = item.getMakanan();
                        if (makanan != null) {
                            Label itemLabel = new Label("• " + makanan.getNama() + " (" + item.getBeratGram() + "g)");
                            itemLabel.setStyle("-fx-text-fill: #34495e;");
                            itemsBox.getChildren().add(itemLabel);
                        }
                    }
                } else {
                    Label noItems = new Label("• Tidak ada makanan terdaftar");
                    noItems.setStyle("-fx-text-fill: #95a5a6; -fx-font-style: italic;");
                    itemsBox.getChildren().add(noItems);
                }

                hariBox.getChildren().add(itemsBox);
                menuBox.getChildren().add(hariBox);
            }
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

    @FXML
    private void onRefresh() {
        loadMenuData();
        loadSekolahData();
    }

    private void showError(String msg) {
        Label err = new Label("Error: " + msg);
        err.setTextFill(Color.RED);
        err.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        dapurListContainer.getChildren().clear();
        dapurListContainer.getChildren().add(err);
    }
}