package com.nutrisense.repositories;

import java.util.List;
import java.util.stream.Collectors;

import com.nutrisense.models.makanan.Makanan;

public class MakananRepository extends JsonRepository<Makanan> {
    
    private static final String MAKANAN_FILE_PATH = "src/main/resources/data/food_database.json";
    private final FoodJsonService foodJsonService;

    public MakananRepository() {
        super(MAKANAN_FILE_PATH, Makanan.class);
        this.foodJsonService = new FoodJsonService();
    }

    // Override findAll to use existing FoodJsonService
    @Override
    public List<Makanan> findAll() {
        return foodJsonService.getAllMakanan();
    }

    // Override other methods to use FoodJsonService
    @Override
    public Makanan findById(String id) {
        // Since FoodJsonService doesn't use IDs, we'll use name as ID
        return foodJsonService.findMakananByName(id);
    }

    public Makanan findByNama(String nama) {
        return foodJsonService.findMakananByName(nama);
    }

    public List<Makanan> searchByNama(String query) {
        return foodJsonService.getAllMakanan().stream()
                .filter(makanan -> makanan.getNama().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<String> searchMakananNames(String query) {
        return foodJsonService.searchMakananNames(query);
    }

    // These methods need implementation for JsonRepository abstract methods
    @Override
    protected Makanan parseItem(String jsonItem) {
        // Not needed since we're using FoodJsonService
        return null;
    }

    @Override
    protected String convertItemToJson(Makanan item) {
        // Not needed since we're using FoodJsonService
        return "";
    }

    // Override save methods to avoid modifying original food database
    @Override
    public void save(Makanan item) {
        System.out.println("MakananRepository: Save operation not supported for read-only food database");
    }

    @Override
    public void saveAll(List<Makanan> items) {
        System.out.println("MakananRepository: SaveAll operation not supported for read-only food database");
    }
}