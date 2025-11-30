package com.nutrisense.controllers.user_umum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nutrisense.controllers.main.MainController;
import com.nutrisense.models.makanan.MenuMBG;
import com.nutrisense.models.makanan.MenuMakanan;
import com.nutrisense.models.makanan.Makanan;
import com.nutrisense.models.user.Sekolah;
import com.nutrisense.models.user.DapurMBG;
import com.nutrisense.repositories.SekolahRepository;
import com.nutrisense.repositories.UserRepository;
import com.nutrisense.utils.LocalDateAdapter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    private Map<String, String> dapurMap = new HashMap<>();
    private final Gson gson = createGson();
    private SekolahRepository sekolahRepository;
    private UserRepository userRepository;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        userRepository = new UserRepository();
        loadSekolahData();
        loadDapurData();
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

    private void loadDapurData() {
        try {
            List<DapurMBG> semuaDapur = userRepository.findAllDapur();
            System.out.println("🔍 Found " + semuaDapur.size() + " dapur in users.json");
            
            // CLEAR previous mappings
            dapurMap.clear();
            
            // PRIMARY MAPPING: ID -> NamaDapur (karena di menus.json pakai DAP001, DAP002, etc)
            for (DapurMBG dapur : semuaDapur) {
                dapurMap.put(dapur.getId(), dapur.getNamaDapur());
                System.out.println("🔧 Mapped: " + dapur.getId() + " -> " + dapur.getNamaDapur());
            }
            
            System.out.println("✅ Total mappings from users.json: " + dapurMap.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ CRITICAL: Gagal memuat data dapur dari users.json");
            showError("Gagal memuat data dapur");
        }
    }

    private String getNamaDapur(String dapurId) {
        if (dapurId == null) {
            System.out.println("❌ dapurId is NULL");
            return "Dapur Tidak Diketahui";
        }
        
        String namaDapur = dapurMap.get(dapurId);
        
        if (namaDapur == null) {
            System.out.println("🚨 NO MAPPING FOUND for dapurId: " + dapurId);
            System.out.println("   Available mappings: " + dapurMap.keySet());
            
            // CRITICAL: Coba cari di users.json lagi
            namaDapur = findDapurNameInUsersJson(dapurId);
            if (namaDapur != null) {
                // Cache the found mapping
                dapurMap.put(dapurId, namaDapur);
                System.out.println("🎯 Found via direct search: " + dapurId + " -> " + namaDapur);
            }
        }
        
        return namaDapur != null ? namaDapur : "Dapur " + dapurId;
    }

    private String findDapurNameInUsersJson(String dapurId) {
        try {
            List<DapurMBG> semuaDapur = userRepository.findAllDapur();
            
            // Cari dengan berbagai kemungkinan
            for (DapurMBG dapur : semuaDapur) {
                // 1. Exact match by ID
                if (dapurId.equals(dapur.getId())) {
                    return dapur.getNamaDapur();
                }
                // 2. Match by username
                if (dapurId.equals(dapur.getUsername())) {
                    return dapur.getNamaDapur();
                }
                // 3. Pattern matching
                if (dapurId.contains(dapur.getId()) || dapur.getId().contains(dapurId)) {
                    return dapur.getNamaDapur();
                }
            }
            
            System.out.println("🔍 Direct search failed for: " + dapurId);
            return null;
            
        } catch (Exception e) {
            System.out.println("❌ Error in direct search for: " + dapurId);
            return null;
        }
    }

    private void loadMenuData() {
        try (InputStream is = getClass().getResourceAsStream("/data/menus.json");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            semuaMenu = gson.fromJson(reader, new TypeToken<List<MenuMBG>>(){}.getType());
            System.out.println("✅ BERHASIL LOAD " + semuaMenu.size() + " MENU DARI menus.json");
            
            // VALIDASI MAPPING DAPUR
            validateDapurMapping();
            
            Platform.runLater(this::tampilkanSemuaDapur);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal memuat data menu MBG: " + e.getMessage());
        }
    }

    private void validateDapurMapping() {
        System.out.println("=== VALIDATING DAPUR MAPPING ===");
        
        Set<String> uniqueDapurIds = semuaMenu.stream()
            .map(MenuMBG::getDapurId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
            
        System.out.println("Unique dapurIds in menus.json: " + uniqueDapurIds);
        System.out.println("Available mappings: " + dapurMap.keySet());
        
        boolean allMapped = true;
        for (String dapurId : uniqueDapurIds) {
            boolean mapped = dapurMap.containsKey(dapurId);
            System.out.println("  " + dapurId + " -> " + getNamaDapur(dapurId) + " [MAPPED: " + mapped + "]");
            if (!mapped) {
                allMapped = false;
            }
        }
        
        if (allMapped) {
            System.out.println("🎉 ALL DAPUR IDs SUCCESSFULLY MAPPED FROM users.json!");
        } else {
            System.out.println("⚠️ SOME DAPUR IDs NOT MAPPED - using fallback names");
        }
        
        System.out.println("=== END VALIDATION ===");
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            String query = newVal == null ? "" : newVal.toLowerCase().trim();
            if (query.isEmpty()) {
                tampilkanSemuaDapur();
            } else {
                List<MenuMBG> filtered = semuaMenu.stream()
                    .filter(menu -> {
                        String namaSekolah = sekolahMap.getOrDefault(menu.getSekolahId(), "").toLowerCase();
                        String dapurId = menu.getDapurId() != null ? menu.getDapurId().toLowerCase() : "";
                        String namaDapur = getNamaDapur(menu.getDapurId()).toLowerCase();
                        String namaMenu = menu.getNamaMenu() != null ? menu.getNamaMenu().toLowerCase() : "";
                        
                        return namaSekolah.contains(query) || 
                               dapurId.contains(query) || 
                               namaDapur.contains(query) ||
                               namaMenu.contains(query);
                    })
                    .collect(Collectors.toList());
                tampilkanDapurList(filtered);
            }
        });
    }

    private void tampilkanSemuaDapur() {
        tampilkanDapurList(semuaMenu);
    }

    private void tampilkanDapurList(List<MenuMBG> menus) {
        dapurListContainer.getChildren().clear();
        placeholder.setVisible(false);

        if (menus.isEmpty()) {
            Label noResult = new Label("Tidak ditemukan menu dengan kata kunci tersebut");
            noResult.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
            dapurListContainer.getChildren().add(noResult);
            return;
        }

        // GROUP BY DAPUR_ID + SEKOLAH_ID
        Map<String, List<MenuMBG>> menusByDapurSekolah = menus.stream()
            .filter(menu -> menu.getDapurId() != null && menu.getSekolahId() != null)
            .collect(Collectors.groupingBy(menu -> 
                menu.getDapurId() + "|" + menu.getSekolahId()
            ));

        System.out.println("🎯 Displaying " + menusByDapurSekolah.size() + " dapur-sekolah combinations");

        for (Map.Entry<String, List<MenuMBG>> entry : menusByDapurSekolah.entrySet()) {
            String[] keys = entry.getKey().split("\\|");
            String dapurId = keys[0];
            String sekolahId = keys[1];
            List<MenuMBG> menusDapurSekolah = entry.getValue();
            
            VBox card = buatCardDapur(dapurId, sekolahId, menusDapurSekolah);
            dapurListContainer.getChildren().add(card);
        }
    }

    private VBox buatCardDapur(String dapurId, String sekolahId, List<MenuMBG> menusDapurSekolah) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-border-radius: 20; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 15, 0, 0, 6);");
        card.setOnMouseClicked(e -> showDetailDapur(dapurId, sekolahId, menusDapurSekolah));
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-background-color: #f8f9fa;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-background-color: #f8f9fa;", "")));

        String namaSekolah = sekolahMap.getOrDefault(sekolahId, "Sekolah Tidak Diketahui");
        String namaDapur = getNamaDapur(dapurId);

        System.out.println("🎨 Creating card: " + dapurId + " -> " + namaDapur);

        // Header: Nama Dapur + Sekolah
        Label lblDapur = new Label("🏭 " + namaDapur);
        lblDapur.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lblSekolah = new Label("🏫 " + namaSekolah);
        lblSekolah.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        // Info box: jumlah hari dan status
        HBox infoBox = new HBox(15);
        
        long hariCount = menusDapurSekolah.stream()
            .filter(MenuMBG::isPublished)
            .map(MenuMBG::getTanggal)
            .distinct()
            .count();

        long totalMenu = menusDapurSekolah.stream()
            .filter(MenuMBG::isPublished)
            .count();

        Label hariCountLabel = new Label("📅 " + hariCount + " hari • 🍽️ " + totalMenu + " menu");
        hariCountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60; -fx-background-color: #d5f4e6; -fx-padding: 6 12; -fx-background-radius: 20;");

        boolean hasPublished = menusDapurSekolah.stream().anyMatch(MenuMBG::isPublished);
        Label statusLabel = new Label(hasPublished ? "✅ Published" : "⏳ Draft");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + (hasPublished ? "#27ae60" : "#f39c12") + "; -fx-background-color: " + (hasPublished ? "#d5f4e6" : "#fef5e7") + "; -fx-padding: 6 12; -fx-background-radius: 20;");

        infoBox.getChildren().addAll(hariCountLabel, statusLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(lblDapur, lblSekolah, infoBox);
        return card;
    }

    private void showDetailDapur(String dapurId, String sekolahId, List<MenuMBG> menusDapurSekolah) {
        placeholder.setVisible(false);
        dapurListContainer.getChildren().clear();

        String namaSekolah = sekolahMap.getOrDefault(sekolahId, "Sekolah Tidak Diketahui");
        String namaDapur = getNamaDapur(dapurId);

        // HEADER
        Label title = new Label("Menu 7 Hari");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label dapurName = new Label("🏭 " + namaDapur);
        dapurName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label sekolahLabel = new Label("🏫 " + namaSekolah);
        sekolahLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-padding: 5 0 20 0;");

        VBox header = new VBox(8, title, dapurName, sekolahLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 30, 0));

        // KONTEN MENU - hanya yang published
        VBox content = new VBox(20);
        content.setPadding(new Insets(10, 30, 30, 30));

        List<MenuMBG> publishedMenus = menusDapurSekolah.stream()
            .filter(MenuMBG::isPublished)
            .sorted(Comparator.comparing(MenuMBG::getTanggal))
            .collect(Collectors.toList());

        if (publishedMenus.isEmpty()) {
            Label noMenu = new Label("❌ Tidak ada menu yang dipublikasikan untuk kombinasi dapur-sekolah ini");
            noMenu.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
            content.getChildren().add(noMenu);
        } else {
            for (MenuMBG menu : publishedMenus) {
                VBox hariBox = buatMenuCard(menu);
                content.getChildren().add(hariBox);
            }
        }

        // Tambah scroll kalau panjang
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        dapurListContainer.getChildren().addAll(header, scroll);
    }

    private VBox buatMenuCard(MenuMBG menu) {
        VBox hariBox = new VBox(10);
        hariBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 16; -fx-padding: 20; -fx-border-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);");

        // Nama Menu & Tanggal
        String namaMenu = menu.getNamaMenu() != null ? menu.getNamaMenu() : "Menu Harian";
        Label hariLabel = new Label(namaMenu + " • " + menu.getTanggal().format(dateFormatter));
        hariLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Informasi Gizi
        if (menu.getTotalGizi() != null) {
            var gizi = menu.getTotalGizi();
            Label giziLabel = new Label(String.format(
                "🔥 %,d kcal • 💪 %.1fg protein • ⚡ %.1fg lemak • 🍚 %.1fg karbo • 🌿 %.1fg serat",
                Math.round(gizi.getTotalKalori()), 
                gizi.getTotalProtein(), 
                gizi.getTotalLemak(),
                gizi.getTotalKarbohidrat(),
                gizi.getTotalSerat()
            ));
            giziLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #27ae60; -fx-background-color: #d5f4e6; -fx-padding: 10; -fx-background-radius: 12;");
            hariBox.getChildren().add(giziLabel);
        }

        // Daftar Makanan
        VBox menuList = new VBox(6);
        Label itemsTitle = new Label("🍴 Daftar Makanan:");
        itemsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e; -fx-padding: 10 0 5 0;");
        menuList.getChildren().add(itemsTitle);

        if (menu.getItems() != null && !menu.getItems().isEmpty()) {
            for (MenuMakanan item : menu.getItems()) {
                Makanan makanan = item.getMakanan();
                if (makanan != null) {
                    Label itemLabel = new Label("• " + makanan.getNama() + " (" + item.getBeratGram() + "g)");
                    itemLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
                    menuList.getChildren().add(itemLabel);
                }
            }
        } else {
            Label noItems = new Label("• Tidak ada makanan terdaftar");
            noItems.setStyle("-fx-font-size: 14px; -fx-text-fill: #95a5a6; -fx-font-style: italic;");
            menuList.getChildren().add(noItems);
        }

        // Urutkan: Hari Label → Gizi → Daftar Makanan
        hariBox.getChildren().add(0, hariLabel);
        if (menu.getTotalGizi() != null) {
            hariBox.getChildren().add(1, hariBox.getChildren().remove(1));
        }
        hariBox.getChildren().add(menuList);

        return hariBox;
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
        err.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        dapurListContainer.getChildren().clear();
        dapurListContainer.getChildren().add(err);
    }
}