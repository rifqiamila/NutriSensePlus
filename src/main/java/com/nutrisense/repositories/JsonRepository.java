package com.nutrisense.repositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository<T> {
    protected final String filePath;
    protected final Class<T> type;

    public JsonRepository(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
        ensureFileExists();
    }

    protected void ensureFileExists() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                Files.createDirectories(Paths.get(file.getParent()));
                Files.write(Paths.get(filePath), "[]".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create JSON file: " + filePath, e);
        }
    }

    public List<T> findAll() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            return parseJsonManually(jsonContent);
        } catch (IOException e) {
            System.err.println("Error reading from " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveAll(List<T> items) {
        try {
            String json = convertToJson(items);
            Files.write(Paths.get(filePath), json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to " + filePath, e);
        }
    }

    public T findById(String id) {
        return findAll().stream()
                .filter(item -> getItemId(item).equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(T item) {
        List<T> items = findAll();
        
        String itemId = getItemId(item);
        for (int i = 0; i < items.size(); i++) {
            if (getItemId(items.get(i)).equals(itemId)) {
                items.set(i, item);
                saveAll(items);
                return;
            }
        }
        
        items.add(item);
        saveAll(items);
    }

    public void delete(String id) {
        List<T> items = findAll();
        items.removeIf(item -> getItemId(item).equals(id));
        saveAll(items);
    }

    // Manual JSON parsing (similar to your FoodJsonService)
    @SuppressWarnings("unchecked")
    private List<T> parseJsonManually(String json) {
        List<T> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return result;
        }

        try {
            String cleanJson = json.trim();
            if (cleanJson.startsWith("[")) cleanJson = cleanJson.substring(1);
            if (cleanJson.endsWith("]")) cleanJson = cleanJson.substring(0, cleanJson.length() - 1);
            
            String[] items = cleanJson.split("\\},\\s*\\{");
            
            for (String item : items) {
                String cleanItem = item.trim();
                if (cleanItem.startsWith("{")) cleanItem = cleanItem.substring(1);
                if (cleanItem.endsWith("}")) cleanItem = cleanItem.substring(0, cleanItem.length() - 1);
                
                T obj = parseItem(cleanItem);
                if (obj != null) {
                    result.add(obj);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
        
        return result;
    }

    // Manual JSON serialization
    private String convertToJson(List<T> items) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            json.append(convertItemToJson(items.get(i)));
            if (i < items.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    // These methods need to be implemented by child classes
    protected T parseItem(String jsonItem) {
        throw new UnsupportedOperationException("parseItem must be implemented by child class");
    }

    protected String convertItemToJson(T item) {
        throw new UnsupportedOperationException("convertItemToJson must be implemented by child class");
    }

    private String getItemId(T item) {
        try {
            var idField = item.getClass().getMethod("getId");
            return (String) idField.invoke(item);
        } catch (Exception e) {
            throw new RuntimeException("Model must have getId() method", e);
        }
    }
}