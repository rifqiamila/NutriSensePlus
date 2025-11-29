package com.nutrisense.controllers.user_umum;

import java.net.URL;
import java.util.ResourceBundle;

import com.nutrisense.controllers.main.MainController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class AKGController implements Initializable {

    private MainController mainController;
    
    @FXML private TableView<AKGData> akgTableView;
    @FXML private ComboBox<String> kategoriComboBox;
    @FXML private Label judulLabel;
    @FXML private VBox containerVBox;
    
    // Data untuk tabel AKG
    private ObservableList<AKGData> akgData = FXCollections.observableArrayList();
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBox();
        setupTableView();
        loadDefaultData();
    }

    private void setupComboBox() {
        kategoriComboBox.setItems(FXCollections.observableArrayList(
            "Bayi & Balita",
            "Anak-anak", 
            "Remaja",
            "Dewasa",
            "Lansia",
            "Ibu Hamil & Menyusui"
        ));
        
        kategoriComboBox.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    loadDataByKategori(newValue);
                }
            }
        );
        
        kategoriComboBox.getSelectionModel().selectFirst();
    }

    private void setupTableView() {
        // Setup columns
        TableColumn<AKGData, String> kelompokCol = new TableColumn<>("Kelompok Usia");
        kelompokCol.setCellValueFactory(cellData -> cellData.getValue().kelompokProperty());
        kelompokCol.setPrefWidth(200);

        TableColumn<AKGData, String> energiCol = new TableColumn<>("Energi (kkal)");
        energiCol.setCellValueFactory(cellData -> cellData.getValue().energiProperty());
        energiCol.setPrefWidth(100);

        TableColumn<AKGData, String> proteinCol = new TableColumn<>("Protein (g)");
        proteinCol.setCellValueFactory(cellData -> cellData.getValue().proteinProperty());
        proteinCol.setPrefWidth(90);

        TableColumn<AKGData, String> lemakCol = new TableColumn<>("Lemak (g)");
        lemakCol.setCellValueFactory(cellData -> cellData.getValue().lemakProperty());
        lemakCol.setPrefWidth(80);

        TableColumn<AKGData, String> karboCol = new TableColumn<>("Karbohidrat (g)");
        karboCol.setCellValueFactory(cellData -> cellData.getValue().karbohidratProperty());
        karboCol.setPrefWidth(120);

        TableColumn<AKGData, String> seratCol = new TableColumn<>("Serat (g)");
        seratCol.setCellValueFactory(cellData -> cellData.getValue().seratProperty());
        seratCol.setPrefWidth(80);

        TableColumn<AKGData, String> airCol = new TableColumn<>("Air (ml)");
        airCol.setCellValueFactory(cellData -> cellData.getValue().airProperty());
        airCol.setPrefWidth(80);

        akgTableView.getColumns().addAll(kelompokCol, energiCol, proteinCol, lemakCol, karboCol, seratCol, airCol);
        akgTableView.setItems(akgData);
    }

    private void loadDefaultData() {
        loadDataByKategori("Bayi & Balita");
    }

    private void loadDataByKategori(String kategori) {
        akgData.clear();
        
        switch (kategori) {
            case "Bayi & Balita":
                loadDataBayiBalita();
                break;
            case "Anak-anak":
                loadDataAnakAnak();
                break;
            case "Remaja":
                loadDataRemaja();
                break;
            case "Dewasa":
                loadDataDewasa();
                break;
            case "Lansia":
                loadDataLansia();
                break;
            case "Ibu Hamil & Menyusui":
                loadDataIbuHamilMenyusui();
                break;
        }
        
        judulLabel.setText("Angka Kecukupan Gizi - " + kategori);
    }

    private void loadDataBayiBalita() {
        akgData.add(new AKGData("Bayi 0-5 bulan", "550", "9", "31", "58", "-", "800"));
        akgData.add(new AKGData("Bayi 6-11 bulan", "800", "15", "35", "105", "-", "1200"));
        akgData.add(new AKGData("Anak 1-3 tahun", "1350", "20", "45", "215", "16", "1500"));
        akgData.add(new AKGData("Anak 4-6 tahun", "1400", "25", "50", "220", "22", "1700"));
    }

    private void loadDataAnakAnak() {
        akgData.add(new AKGData("Anak 7-9 tahun (L)", "1650", "35", "55", "250", "26", "1900"));
        akgData.add(new AKGData("Anak 7-9 tahun (P)", "1550", "35", "50", "240", "23", "1800"));
    }

    private void loadDataRemaja() {
        akgData.add(new AKGData("Remaja 10-12 tahun (L)", "2000", "50", "65", "300", "30", "2000"));
        akgData.add(new AKGData("Remaja 10-12 tahun (P)", "1900", "55", "65", "280", "28", "1900"));
        akgData.add(new AKGData("Remaja 13-15 tahun (L)", "2400", "70", "80", "360", "35", "2200"));
        akgData.add(new AKGData("Remaja 13-15 tahun (P)", "1950", "65", "70", "290", "30", "2000"));
        akgData.add(new AKGData("Remaja 16-18 tahun (L)", "2650", "75", "85", "400", "37", "2500"));
        akgData.add(new AKGData("Remaja 16-18 tahun (P)", "2100", "65", "70", "320", "32", "2100"));
    }

    private void loadDataDewasa() {
        akgData.add(new AKGData("Dewasa 19-29 tahun (L)", "2650", "65", "75", "430", "37", "2500"));
        akgData.add(new AKGData("Dewasa 19-29 tahun (P)", "2250", "60", "65", "360", "32", "2300"));
        akgData.add(new AKGData("Dewasa 30-49 tahun (L)", "2550", "65", "70", "415", "36", "2500"));
        akgData.add(new AKGData("Dewasa 30-49 tahun (P)", "2150", "60", "65", "340", "30", "2300"));
    }

    private void loadDataLansia() {
        akgData.add(new AKGData("Lansia 50-64 tahun (L)", "2150", "65", "60", "340", "30", "2300"));
        akgData.add(new AKGData("Lansia 50-64 tahun (P)", "1800", "60", "55", "280", "25", "1900"));
        akgData.add(new AKGData("Lansia 65+ tahun (L)", "1800", "65", "50", "280", "25", "1900"));
        akgData.add(new AKGData("Lansia 65+ tahun (P)", "1550", "60", "45", "240", "22", "1600"));
    }

    private void loadDataIbuHamilMenyusui() {
        akgData.add(new AKGData("Ibu Hamil Trimester 1", "+180", "+1", "+2.3", "+25", "+2", "+300"));
        akgData.add(new AKGData("Ibu Hamil Trimester 2", "+300", "+10", "+2.3", "+40", "+3", "+400"));
        akgData.add(new AKGData("Ibu Hamil Trimester 3", "+300", "+30", "+2.3", "+40", "+3", "+400"));
        akgData.add(new AKGData("Ibu Menyusui 0-6 bulan", "+330", "+20", "+2.3", "+45", "+4", "+800"));
        akgData.add(new AKGData("Ibu Menyusui 7-12 bulan", "+400", "+15", "+2.3", "+55", "+4", "+650"));
    }

    @FXML
    private void onBack() {
        if (mainController != null) {
            mainController.loadPage("/fxml/user_umum/home.fxml");
        }
    }

    // Data model class
    public static class AKGData {
        private final SimpleStringProperty kelompok;
        private final SimpleStringProperty energi;
        private final SimpleStringProperty protein;
        private final SimpleStringProperty lemak;
        private final SimpleStringProperty karbohidrat;
        private final SimpleStringProperty serat;
        private final SimpleStringProperty air;

        public AKGData(String kelompok, String energi, String protein, String lemak, 
                      String karbohidrat, String serat, String air) {
            this.kelompok = new SimpleStringProperty(kelompok);
            this.energi = new SimpleStringProperty(energi);
            this.protein = new SimpleStringProperty(protein);
            this.lemak = new SimpleStringProperty(lemak);
            this.karbohidrat = new SimpleStringProperty(karbohidrat);
            this.serat = new SimpleStringProperty(serat);
            this.air = new SimpleStringProperty(air);
        }

        // Property methods
        public SimpleStringProperty kelompokProperty() { return kelompok; }
        public SimpleStringProperty energiProperty() { return energi; }
        public SimpleStringProperty proteinProperty() { return protein; }
        public SimpleStringProperty lemakProperty() { return lemak; }
        public SimpleStringProperty karbohidratProperty() { return karbohidrat; }
        public SimpleStringProperty seratProperty() { return serat; }
        public SimpleStringProperty airProperty() { return air; }

        // Getter methods
        public String getKelompok() { return kelompok.get(); }
        public String getEnergi() { return energi.get(); }
        public String getProtein() { return protein.get(); }
        public String getLemak() { return lemak.get(); }
        public String getKarbohidrat() { return karbohidrat.get(); }
        public String getSerat() { return serat.get(); }
        public String getAir() { return air.get(); }
    }
}