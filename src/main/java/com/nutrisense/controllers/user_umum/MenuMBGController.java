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
    private final Gson gson = createGson();
    private SekolahRepository sekolahRepository;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // BARU: Untuk support JSON baru (1 dapur → banyak sekolah)
    private List<DapurWrapper> daftarDapurJson = new ArrayList<>();

    // Inner class sementara biar gak ubah struktur
    private static class DapurWrapper {
        String dapurId;
        String namaDapur;
        Object sekolah; // bisa String atau List<String>
        List<MenuHarian> menu7Hari;

        static class MenuHarian {
            String tanggal, hari;
            List<String> menu;
            Gizi gizi;
            static class Gizi {
                int kalori;
                double protein, lemak, karbohidrat, serat;
            }
        }
    }

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
        loadMenuData(); // sekarang bisa baca JSON baru
        setupSearch();
    }

    private void loadSekolahData() {
        try {
            List<Sekolah> semuaSekolah = sekolahRepository.findAll();
            for (Sekolah sekolah : semuaSekolah) {
                sekolahMap.put(sekolah.getId(), sekolah.getNama());
            }
            System.out.println("Loaded " + sekolahMap.size() + " schools");
        } catch (Exception e) {
        e.printStackTrace();
        }
    }

    private void loadMenuData() {
        try (InputStream is = getClass().getResourceAsStream("/data/menu_mbg.json");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            // BACA JSON BARU: struktur dapur → banyak sekolah
            daftarDapurJson = gson.fromJson(reader, new TypeToken<List<DapurWrapper>>(){}.getType());
            System.out.println("BERHASIL LOAD " + daftarDapurJson.size() + " DAPUR DARI JSON BARU");

            // Kalau mau tetap support menu lama dari DB, tinggal uncomment
            // semuaMenu = gson.fromJson(reader, new TypeToken<List<MenuMBG>>(){}.getType());

            Platform.runLater(this::tampilkanSemuaDapur);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal baca menu_mbg.json: " + e.getMessage());
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            String query = newVal == null ? "" : newVal.toLowerCase().trim();
            if (query.isEmpty()) {
                tampilkanSemuaDapur();
            } else {
                List<DapurWrapper> filtered = daftarDapurJson.stream()
                    .filter(d -> {
                        String namaDapur = (d.namaDapur != null ? d.namaDapur : d.dapurId).toLowerCase();
                        String sekolahText = sekolahListToString(d.sekolah).toLowerCase();
                        return namaDapur.contains(query) || sekolahText.contains(query);
                    })
                    .collect(Collectors.toList());
                tampilkanDapurList(filtered);
            }
        });
    }

    private void tampilkanSemuaDapur() {
        tampilkanDapurList(daftarDapurJson);
    }

    private void tampilkanDapurList(List<DapurWrapper> list) {
        dapurListContainer.getChildren().clear();
        placeholder.setVisible(false);

        if (list.isEmpty()) {
            Label noResult = new Label("Tidak ditemukan dapur dengan kata kunci tersebut");
            noResult.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
            dapurListContainer.getChildren().add(noResult);
            return;
        }

        for (DapurWrapper dapur : list) {
            VBox card = buatCardDapur(dapur);
            dapurListContainer.getChildren().add(card);
        }
    }

private VBox buatCardDapur(DapurWrapper dapur) {
    VBox card = new VBox(14);
    card.setPadding(new Insets(24));
    
    // HAPUS SEMUA INLINE STYLE PUTIH!!! GANTI PAKE STYLECLASS AJA
    card.getStyleClass().add("dapur-card");
    
    // Hover effect pake CSS doang (lebih smooth)
    card.setOnMouseEntered(e -> card.getStyleClass().add("dapur-card-hover"));
    card.setOnMouseExited(e -> card.getStyleClass().remove("dapur-card-hover"));
    card.setOnMouseClicked(e -> showDetailDapur(dapur));

    String namaDapur = dapur.namaDapur != null ? dapur.namaDapur : "Dapur MBG";

    Label lblDapur = new Label(namaDapur);
    lblDapur.getStyleClass().add("dapur-title");  // pake class, bukan inline

    Label lblSekolah = new Label(sekolahListToString(dapur.sekolah));
    lblSekolah.getStyleClass().add("dapur-sekolah");

    HBox infoBox = new HBox(20);
    infoBox.setAlignment(Pos.CENTER_LEFT);

    Label hariCount = new Label(dapur.menu7Hari.size() + " hari menu");
    hariCount.getStyleClass().add("dapur-button");

    Label status = new Label("Published");
    status.getStyleClass().add("dapur-button");

    infoBox.getChildren().addAll(hariCount, status);

    card.getChildren().addAll(lblDapur, lblSekolah, infoBox);
    return card;
}

    private String sekolahListToString(Object sekolahObj) {
        if (sekolahObj == null) return "Sekolah Tidak Diketahui";
        if (sekolahObj instanceof String) {
            return (String) sekolahObj;
        }
        if (sekolahObj instanceof List) {
            return String.join(" • ", (List<String>) sekolahObj);
        }
        return "Sekolah Tidak Diketahui";
    }

   private void showDetailDapur(DapurWrapper dapur) {
    placeholder.setVisible(false);
    dapurListContainer.getChildren().clear();

    String namaDapur = dapur.namaDapur != null ? dapur.namaDapur : "Dapur MBG";
    String semuaSekolah = sekolahListToString(dapur.sekolah);

    // HEADER GLOWING
    Label title = new Label("Menu 7 Hari Minggu Ini");
    title.getStyleClass().add("detail-title");

    Label dapurName = new Label(namaDapur);
    dapurName.getStyleClass().add("dapur-name");

    Label sekolahLabel = new Label(semuaSekolah);
    sekolahLabel.getStyleClass().add("sekolah-label");

    VBox header = new VBox(10, title, dapurName, sekolahLabel);
    header.setAlignment(Pos.CENTER);
    header.setPadding(new Insets(30, 0, 40, 0));

    // KONTEN MENU — GUNAKAN CLASS, BUKAN INLINE STYLE!!!
    VBox content = new VBox(22);
    content.setPadding(new Insets(10, 40, 40, 40));

    for (DapurWrapper.MenuHarian hari : dapur.menu7Hari) {
        VBox hariBox = new VBox(14);
        hariBox.getStyleClass().add("menu-harian-card");  // INI YANG PENTING!!!

        Label hariLabel = new Label(hari.hari + "  •  " + formatTanggal(hari.tanggal));
        hariLabel.getStyleClass().add("hari-title");

        VBox menuList = new VBox(8);
        for (String menu : hari.menu) {
            Label item = new Label("•  " + menu);
            item.getStyleClass().add("menu-item");
            menuList.getChildren().add(item);
        }

        Label gizi = new Label(String.format("Kalori: %,d kcal • Protein: %.1fg • Lemak: %.1fg • Karbo: %.1fg • Serat: %.1fg",
                hari.gizi.kalori, hari.gizi.protein, hari.gizi.lemak, hari.gizi.karbohidrat, hari.gizi.serat));
        gizi.getStyleClass().add("nutrisi-chip");

        hariBox.getChildren().addAll(hariLabel, menuList, gizi);
        content.getChildren().add(hariBox);
    }

    ScrollPane scroll = new ScrollPane(content);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

    dapurListContainer.getChildren().addAll(header, scroll);
}

    private String formatTanggal(String iso) {
        try {
            LocalDate date = LocalDate.parse(iso);
            return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID")));
        } catch (Exception e) {
            return iso;
        }
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