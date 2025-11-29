package com.nutrisense.repositories;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.nutrisense.models.makanan.Makanan;

public class FoodJsonService {
    private static final String JSON_FILE = "/data/food_database.json";
    private List<Makanan> makananList;
    
    public FoodJsonService() {
        loadData();
    }
    
    private void loadData() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(JSON_FILE);
            if (inputStream == null) {
                System.out.println("File JSON tidak ditemukan: " + JSON_FILE);
                makananList = new ArrayList<>();
                return;
            }
            
            // Manual JSON parsing
            String jsonContent = new Scanner(inputStream).useDelimiter("\\A").next();
            makananList = parseJsonManually(jsonContent);
            
            System.out.println("Loaded " + makananList.size() + " makanan from JSON");
            
        } catch (Exception e) {
            System.out.println("Error loading JSON: " + e.getMessage());
            makananList = new ArrayList<>();
        }
    }
    
    private List<Makanan> parseJsonManually(String json) {
        List<Makanan> result = new ArrayList<>();
        String[] items = json.split("\\},\\s*\\{");
        
        for (String item : items) {
            try {
                // Clean up JSON syntax
                item = item.replace("[", "").replace("]", "").replace("{", "").replace("}", "").trim();
                
                String nama = extractValue(item, "nama");
                double kalori = Double.parseDouble(extractValue(item, "kalori"));
                double protein = Double.parseDouble(extractValue(item, "protein"));
                double karbohidrat = Double.parseDouble(extractValue(item, "karbohidrat"));
                double lemak = Double.parseDouble(extractValue(item, "lemak"));
                double serat = Double.parseDouble(extractValue(item, "serat"));
                
                result.add(new Makanan(nama, kalori, protein, karbohidrat, lemak, serat));
            } catch (Exception e) {
                System.out.println("Error parsing item: " + item);
            }
        }
        
        return result;
    }
    
    private String extractValue(String jsonItem, String key) {
        String search = "\"" + key + "\":";
        int start = jsonItem.indexOf(search);
        if (start == -1) return "";
        
        start += search.length();
        int end = jsonItem.indexOf(",", start);
        if (end == -1) end = jsonItem.length();
        
        String value = jsonItem.substring(start, end).trim();
        
        // Remove quotes if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        
        return value;
    }
    
    // Method lainnya tetap sama...
    public Makanan findMakananByName(String nama) {
        return makananList.stream()
                .filter(m -> m.getNama().toLowerCase().contains(nama.toLowerCase()))
                .findFirst()
                .orElse(null);
    }

    public List<String> searchMakananNames(String query) {
        List<String> results = new ArrayList<>();
        for (Makanan makanan : makananList) {
            if (makanan.getNama().toLowerCase().contains(query.toLowerCase())) {
                results.add(makanan.getNama());
            }
        }
        return results;
    }

    public List<Makanan> getAllMakanan() {
        return new ArrayList<>(makananList);
    }
}