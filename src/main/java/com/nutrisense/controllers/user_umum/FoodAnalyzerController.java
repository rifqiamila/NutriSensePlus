package com.nutrisense.controllers.user_umum;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.nutrisense.controllers.main.MainController;
import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.Makanan;
import com.nutrisense.services.SimpleFoodAnalyzer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class FoodAnalyzerController implements Initializable {

    private MainController mainController;
    private SimpleFoodAnalyzer foodAnalyzer;
    
    // Data models
    private ObservableList<Makanan> allFoods;
    private ObservableList<String> foodNames;
    private ObservableList<FoodEntry> selectedFoods = FXCollections.observableArrayList();
    
    @FXML private ComboBox<String> foodComboBox;
    @FXML private TextField gramsField;
    @FXML private TableView<FoodEntry> foodsTableView;
    @FXML private Label selectedCountLabel;
    @FXML private Button analyzeButton;
    @FXML private PieChart pieChart;
    @FXML private VBox resultsSection;
    @FXML private Label kaloriLabel;
    @FXML private Label proteinLabel;
    @FXML private Label karbohidratLabel;
    @FXML private Label lemakLabel;
    @FXML private Label seratLabel;
    @FXML private VBox safetyAlertsSection;
    @FXML private VBox alertsContainer;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeFoodAnalyzer();
        setupComboBox();
        setupTableView();
        setupEventHandlers();
        updateSelectedCount();
    }

    private void initializeFoodAnalyzer() {
        try {
            this.foodAnalyzer = new SimpleFoodAnalyzer();
            List<Makanan> allFoodsList = foodAnalyzer.getFoodRepo().getAllMakanan();
            this.allFoods = FXCollections.observableArrayList(allFoodsList);
            
            // Extract food names
            this.foodNames = FXCollections.observableArrayList();
            for (Makanan makanan : allFoods) {
                foodNames.add(makanan.getNama());
            }
            
            System.out.println("Loaded " + allFoods.size() + " foods from database");
            
        } catch (Exception e) {
            System.err.println("Error initializing food analyzer: " + e.getMessage());
            e.printStackTrace();
            this.allFoods = FXCollections.observableArrayList();
            this.foodNames = FXCollections.observableArrayList();
        }
    }

    private void setupComboBox() {
        // Set items dengan semua nama makanan
        foodComboBox.setItems(foodNames);
        foodComboBox.setEditable(true);
        
        // Setup auto-complete filtering sederhana
        foodComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                foodComboBox.setItems(foodNames);
            } else {
                String filter = newValue.toLowerCase();
                ObservableList<String> filteredItems = FXCollections.observableArrayList();
                
                for (String foodName : foodNames) {
                    if (foodName.toLowerCase().contains(filter)) {
                        filteredItems.add(foodName);
                    }
                }
                foodComboBox.setItems(filteredItems);
            }
            
            // Show dropdown jika ada item
            if (!foodComboBox.getItems().isEmpty()) {
                foodComboBox.show();
            }
        });

        // Handle Enter key - langsung tambah makanan
        foodComboBox.getEditor().setOnAction(event -> {
            onAddFood();
        });

        // Handle selection dari dropdown
        foodComboBox.setOnAction(event -> {
            if (foodComboBox.getValue() != null) {
                gramsField.requestFocus();
                gramsField.selectAll();
            }
        });
    }

    private Makanan findMakananByName(String foodName) {
        if (foodName == null || foodName.trim().isEmpty()) {
            return null;
        }
        
        // Cari exact match dulu
        for (Makanan makanan : allFoods) {
            if (makanan.getNama().equalsIgnoreCase(foodName.trim())) {
                return makanan;
            }
        }
        
        // Jika tidak ketemu, cari contains (case insensitive)
        for (Makanan makanan : allFoods) {
            if (makanan.getNama().toLowerCase().contains(foodName.toLowerCase().trim())) {
                return makanan;
            }
        }
        
        return null;
    }

    private void setupTableView() {
        foodsTableView.setItems(selectedFoods);
        
        // Clear existing columns
        foodsTableView.getColumns().clear();
        
        // Setup columns
        TableColumn<FoodEntry, String> namaCol = new TableColumn<>("Makanan");
        namaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        namaCol.setPrefWidth(200);
        
        TableColumn<FoodEntry, String> beratCol = new TableColumn<>("Berat (g)");
        beratCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.1f", cellData.getValue().getBerat())));
        beratCol.setPrefWidth(100);
        
        TableColumn<FoodEntry, Button> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setCellValueFactory(cellData -> {
            Button deleteBtn = new Button("X");
            deleteBtn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand; -fx-font-family: 'Segoe UI Emoji';");
            deleteBtn.setOnAction(e -> {
                FoodEntry entry = cellData.getValue();
                selectedFoods.remove(entry);
                updateSelectedCount();
            });
            return new SimpleObjectProperty<>(deleteBtn);
        });
        aksiCol.setPrefWidth(80);
        namaCol.setStyle("-fx-text-fill: #00807E; -fx-font-weight: bold; -fx-font-size: 14px;");
        beratCol.setStyle("-fx-text-fill: #00807E; -fx-font-weight: bold; -fx-font-size: 14px;");
        aksiCol.setStyle("-fx-text-fill: #00807E; -fx-font-weight: bold; -fx-font-size: 14px;");
        
        foodsTableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                foodsTableView.lookupAll(".column-header .label").forEach(node -> {
                    node.setStyle("-fx-text-fill: #00807E; -fx-font-weight: bold; -fx-font-size: 14px;");
                });
            }
        });
    }

    private void setupEventHandlers() {
        // Enable analyze button when foods are added
        selectedFoods.addListener((javafx.collections.ListChangeListener.Change<? extends FoodEntry> change) -> {
            updateSelectedCount();
            analyzeButton.setDisable(selectedFoods.isEmpty());
        });
    }

    @FXML
    private void onAddFood() {
        System.out.println("=== onAddFood called ===");
        
        // Dapatkan input dari user
        String inputText = foodComboBox.getEditor().getText();
        String selectedValue = foodComboBox.getValue();
        
        System.out.println("Input text: '" + inputText + "'");
        System.out.println("Selected value: '" + selectedValue + "'");
        
        // Prioritaskan selected value dari dropdown, jika ada
        String foodNameToUse = (selectedValue != null && !selectedValue.isEmpty()) ? selectedValue : inputText;
        
        System.out.println("Food name to use: '" + foodNameToUse + "'");
        
        if (foodNameToUse == null || foodNameToUse.trim().isEmpty()) {
            showAlert("Peringatan", "Silakan pilih atau ketik nama makanan");
            return;
        }
        
        // Cari makanan yang cocok
        Makanan foundMakanan = findMakananByName(foodNameToUse);
        
        if (foundMakanan == null) {
            System.out.println("Makanan tidak ditemukan: '" + foodNameToUse + "'");
            showAlert("Peringatan", 
                "Makanan '" + foodNameToUse + "' tidak ditemukan.\n" +
                "Pastikan mengetik dengan benar atau pilih dari dropdown.");
            return;
        }
        
        System.out.println("Makanan ditemukan: " + foundMakanan.getNama());
        
        // Validasi berat
        String gramsText = gramsField.getText().trim();
        if (gramsText.isEmpty()) {
            showAlert("Peringatan", "Silakan masukkan berat makanan");
            return;
        }
        
        try {
            double grams = Double.parseDouble(gramsText);
            if (grams <= 0) {
                showAlert("Peringatan", "Berat harus lebih dari 0 gram");
                return;
            }
            
            if (grams > 10000) {
                showAlert("Peringatan", "Berat terlalu besar. Maksimal 10,000 gram");
                return;
            }
            
            // Cek duplikat
            for (FoodEntry entry : selectedFoods) {
                if (entry.getMakanan().getNama().equalsIgnoreCase(foundMakanan.getNama())) {
                    showAlert("Peringatan", 
                        foundMakanan.getNama() + " sudah ada dalam daftar.\n" +
                        "Hapus yang lama terlebih dahulu jika ingin mengubah.");
                    return;
                }
            }
            
            // Tambahkan ke daftar
            FoodEntry newEntry = new FoodEntry(foundMakanan, grams);
            selectedFoods.add(newEntry);
            
            // Reset form
            foodComboBox.getSelectionModel().clearSelection();
            foodComboBox.setValue(null);
            foodComboBox.getEditor().clear();
            gramsField.setText("100");
            
            // Reset dropdown items
            foodComboBox.setItems(foodNames);
            
            System.out.println("Berhasil menambahkan: " + foundMakanan.getNama() + " (" + grams + "g)");
            System.out.println("Total makanan sekarang: " + selectedFoods.size());
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Berat harus berupa angka\nContoh: 100, 150.5, 200");
        }
    }

    @FXML
    private void onAnalyze() {
        if (selectedFoods.isEmpty()) {
            showAlert("Peringatan", "Tidak ada makanan untuk dianalisis");
            return;
        }
        
        try {
            // Calculate total nutrition
            GiziResult totalResult = calculateTotalNutrition();
            
            // Display results
            displayResults(totalResult);
            displayNutritionDetails(totalResult);
            checkSafetyAlerts(totalResult);
            
            // Show results section
            resultsSection.setVisible(true);
            
            System.out.println("Analysis completed for " + selectedFoods.size() + " foods");
            
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan saat menganalisis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private GiziResult calculateTotalNutrition() {
        double totalKalori = 0;
        double totalProtein = 0;
        double totalKarbohidrat = 0;
        double totalLemak = 0;
        double totalSerat = 0;
        
        for (FoodEntry entry : selectedFoods) {
            Makanan makanan = entry.getMakanan();
            double berat = entry.getBerat();
            double multiplier = berat / 100.0;
            
            totalKalori += makanan.getKalori() * multiplier;
            totalProtein += makanan.getProtein() * multiplier;
            totalKarbohidrat += makanan.getKarbohidrat() * multiplier;
            totalLemak += makanan.getLemak() * multiplier;
            totalSerat += makanan.getSerat() * multiplier;
        }
        
        return new GiziResult(totalKalori, totalProtein, totalKarbohidrat, totalLemak, totalSerat);
    }

    private void displayResults(GiziResult result) {
        pieChart.getData().clear();
        // pieChart.setTitle("Komposisi Makronutrien");
        pieChart.setLegendVisible(true);
        
        // Create pie chart data (macronutrient distribution)
        double proteinCals = result.getTotalProtein() * 4;
        double carbCals = result.getTotalKarbohidrat() * 4;
        double fatCals = result.getTotalLemak() * 9;
        double totalCals = proteinCals + carbCals + fatCals;
        
        if (totalCals > 0) {
            double proteinPercent = (proteinCals / totalCals) * 100;
            double carbPercent = (carbCals / totalCals) * 100;
            double fatPercent = (fatCals / totalCals) * 100;
            
            PieChart.Data proteinSlice = new PieChart.Data("Protein " + String.format("%.1f%%", proteinPercent), proteinPercent);
            PieChart.Data carbSlice = new PieChart.Data("Karbohidrat " + String.format("%.1f%%", carbPercent), carbPercent);
            PieChart.Data fatSlice = new PieChart.Data("Lemak " + String.format("%.1f%%", fatPercent), fatPercent);
            
            pieChart.getData().addAll(proteinSlice, carbSlice, fatSlice);
            
            // Apply colors after data is added
            for (PieChart.Data data : pieChart.getData()) {
                if (data.getName().startsWith("Protein")) {
                    data.getNode().setStyle("-fx-pie-color: #9b59b6;");
                } else if (data.getName().startsWith("Karbohidrat")) {
                    data.getNode().setStyle("-fx-pie-color: #3498db;");
                } else if (data.getName().startsWith("Lemak")) {
                    data.getNode().setStyle("-fx-pie-color: #e67e22;");
                }
            }
        } else {
            // Handle case when no calories
            PieChart.Data noData = new PieChart.Data("Tidak ada data", 1);
            pieChart.getData().add(noData);
        }
    }
              
    private void displayNutritionDetails(GiziResult result) {
        kaloriLabel.setText(String.format("%.1f kkal", result.getTotalKalori()));
        proteinLabel.setText(String.format("%.1f g", result.getTotalProtein()));
        karbohidratLabel.setText(String.format("%.1f g", result.getTotalKarbohidrat()));
        lemakLabel.setText(String.format("%.1f g", result.getTotalLemak()));
        seratLabel.setText(String.format("%.1f g", result.getTotalSerat()));
    }

    private void checkSafetyAlerts(GiziResult result) {
        alertsContainer.getChildren().clear();
        boolean hasAlerts = false;
        
        // Safety checks
        if (result.getTotalLemak() > 50) {
            addAlert("⚠️ Lemak total tinggi: " + String.format("%.1fg", result.getTotalLemak()) + " (disarankan < 50g)");
            hasAlerts = true;
        }
        
        if (result.getTotalKalori() > 800) {
            addAlert("⚡ Kalori tinggi: " + String.format("%.1fkkal", result.getTotalKalori()) + " (sekali makan)");
            hasAlerts = true;
        }
        
        if (result.getTotalSerat() < 5) {
            addAlert("🌿 Serat rendah: " + String.format("%.1fg", result.getTotalSerat()) + " (tambahkan sayur/buah)");
            hasAlerts = true;
        }
        
        if (result.getTotalProtein() < 10) {
            addAlert("💪 Protein rendah: " + String.format("%.1fg", result.getTotalProtein()) + " (tambahkan sumber protein)");
            hasAlerts = true;
        }
        
        safetyAlertsSection.setVisible(hasAlerts);
    }

    private void addAlert(String message) {
        Label alert = new Label("• " + message);
        alert.setStyle("-fx-text-fill: #856404; -fx-font-size: 15px; -fx-wrap-text: true;");
        alert.setMaxWidth(400);
        alertsContainer.getChildren().add(alert);
    }

    @FXML
    private void onClearAll() {
        if (!selectedFoods.isEmpty()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmasi Hapus");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Apakah Anda yakin ingin menghapus semua makanan dari daftar?");
            
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedFoods.clear();
                updateSelectedCount();
            }
        }
    }

    @FXML
    private void onReset() {
        if (!selectedFoods.isEmpty() || resultsSection.isVisible()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmasi Reset");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Apakah Anda yakin ingin mereset semua input dan hasil?");
            
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedFoods.clear();
                resultsSection.setVisible(false);
                safetyAlertsSection.setVisible(false);
                foodComboBox.getSelectionModel().clearSelection();
                foodComboBox.setValue(null);
                foodComboBox.getEditor().clear();
                gramsField.setText("100");
                foodComboBox.setItems(foodNames); // Reset dropdown items
                updateSelectedCount();
            }
        }
    }

    @FXML
    private void onBack() {
        if (mainController != null) {
            mainController.loadPage("/fxml/user_umum/home.fxml");
        }
    }

    private void updateSelectedCount() {
        int count = selectedFoods.size();
        selectedCountLabel.setText(count + " makanan");
        
        // Update tooltip untuk menunjukkan daftar makanan
        if (count > 0) {
            StringBuilder tooltipText = new StringBuilder("Makanan terpilih:\n");
            for (FoodEntry entry : selectedFoods) {
                tooltipText.append("• ").append(entry.getNama())
                         .append(" (").append(String.format("%.1fg", entry.getBerat())).append(")\n");
            }
            Tooltip tooltip = new Tooltip(tooltipText.toString());
            selectedCountLabel.setTooltip(tooltip);
        } else {
            selectedCountLabel.setTooltip(null);
        }
        
        // Update analyze button state
        analyzeButton.setDisable(selectedFoods.isEmpty());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for table data
    public static class FoodEntry {
        private final Makanan makanan;
        private final double berat;

        public FoodEntry(Makanan makanan, double berat) {
            this.makanan = makanan;
            this.berat = berat;
        }

        public String getNama() { 
            return makanan != null ? makanan.getNama() : "Unknown"; 
        }
        
        public double getBerat() { 
            return berat; 
        }
        
        public Makanan getMakanan() { 
            return makanan; 
        }
        
        @Override
        public String toString() {
            return String.format("%s (%.1fg)", getNama(), getBerat());
        }
    }
}