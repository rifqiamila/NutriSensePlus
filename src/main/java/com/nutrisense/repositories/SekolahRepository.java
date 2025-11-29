package com.nutrisense.repositories;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import com.nutrisense.models.user.Sekolah;

public class SekolahRepository extends JsonRepository<Sekolah> {
    
    private static final String SEKOLAH_FILE_PATH = "src/main/resources/data/sekolah.json";

    public SekolahRepository() {
        super(SEKOLAH_FILE_PATH, Sekolah.class);
    }

    @Override
    protected Sekolah parseItem(String jsonItem) {
        try {
            System.out.println("🔧 Parsing sekolah item: " + jsonItem);
            
            // Extract fields dulu
            String id = extractField(jsonItem, "id");
            String nama = extractField(jsonItem, "nama");
            String alamat = extractField(jsonItem, "alamat");
            String tingkat = extractField(jsonItem, "tingkat");
            
            System.out.println("📦 Extracted - ID: " + id + ", Nama: " + nama + ", Alamat: " + alamat + ", Tingkat: " + tingkat);
            
            // Validasi field yang required
            if (id == null || nama == null || alamat == null || tingkat == null) {
                System.err.println("❌ Missing required fields for sekolah");
                return null;
            }
            
            // Buat object Sekolah dengan constructor yang benar
            Sekolah sekolah = new Sekolah(id, nama, alamat, tingkat);
            
            // Set field aktif
            String aktifStr = extractField(jsonItem, "aktif");
            if (aktifStr != null) {
                sekolah.setAktif(Boolean.parseBoolean(aktifStr));
            }
            // else tetap pakai default value true dari constructor
            
            System.out.println("✅ Successfully created sekolah: " + sekolah.getNama());
            return sekolah;
            
        } catch (Exception e) {
            System.err.println("❌ Error parsing sekolah item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String convertItemToJson(Sekolah item) {
        // Simple manual JSON conversion
        StringBuilder json = new StringBuilder("{");
        
        json.append("\"id\":\"").append(item.getId()).append("\",");
        json.append("\"nama\":\"").append(item.getNama()).append("\",");
        json.append("\"alamat\":\"").append(item.getAlamat()).append("\",");
        json.append("\"tingkat\":\"").append(item.getTingkat()).append("\",");
        json.append("\"aktif\":").append(item.isAktif());
        
        json.append("}");
        return json.toString();
    }

    private String extractField(String json, String fieldName) {
        try {
            String search = "\"" + fieldName + "\":";
            int startIndex = json.indexOf(search);
            if (startIndex == -1) {
                System.out.println("❌ Field '" + fieldName + "' not found in JSON: " + json);
                return null;
            }
            
            startIndex += search.length();
            
            // Cari akhir value (bisa koma atau tutup kurung)
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
            
            if (endIndex == -1) {
                System.out.println("❌ Could not find end of field '" + fieldName + "'");
                return null;
            }
            
            String value = json.substring(startIndex, endIndex).trim();
            
            // Remove quotes jika ada (untuk string fields)
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            
            System.out.println("🔍 Extracted field '" + fieldName + "': " + value);
            return value;
            
        } catch (Exception e) {
            System.err.println("❌ Error extracting field '" + fieldName + "': " + e.getMessage());
            return null;
        }
    }

    // 🔥 OVERRIDE findAll() UNTUK DEBUGGING
    @Override
    public List<Sekolah> findAll() {
        try {
            List<Sekolah> result = super.findAll();
            System.out.println("📊 SekolahRepository.findAll() returned: " + result.size() + " items");
            return result;
        } catch (Exception e) {
            System.err.println("❌ Error in SekolahRepository.findAll(): " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list instead of crashing
        }
    }

    // Sekolah-specific methods
    public List<Sekolah> findAktif() {
        List<Sekolah> allSekolah = findAll();
        System.out.println("🔍 Total schools found: " + allSekolah.size());
        List<Sekolah> aktifSekolah = allSekolah.stream()
                .filter(Sekolah::isAktif)
                .collect(Collectors.toList());
        System.out.println("✅ Active schools: " + aktifSekolah.size());
        return aktifSekolah;
    }

    public List<Sekolah> findByTingkat(String tingkat) {
        return findAll().stream()
                .filter(sekolah -> tingkat.equals(sekolah.getTingkat()))
                .collect(Collectors.toList());
    }

    public List<String> getAllNamaSekolah() {
        List<Sekolah> allSekolah = findAll();
        System.out.println("📚 All sekolah names: " + allSekolah.size());
        return allSekolah.stream()
                .map(Sekolah::getNama)
                .collect(Collectors.toList());
    }

    // 🔥 METHOD BARU UNTUK MAPPING ID -> NAMA
    public String getNamaSekolahById(String sekolahId) {
        List<Sekolah> allSekolah = findAll();
        System.out.println("🔍 Looking for sekolah ID: " + sekolahId);
        System.out.println("📊 Available schools: " + allSekolah.size());
        
        String result = allSekolah.stream()
                .filter(sekolah -> sekolahId.equals(sekolah.getId()))
                .map(Sekolah::getNama)
                .findFirst()
                .orElse("Sekolah Tidak Diketahui");
        
        System.out.println("🎯 Found: " + result);
        return result;
    }

    // 🔥 METHOD BARU UNTUK GET ALL MAPPING
    public Map<String, String> getAllSekolahMapping() {
        List<Sekolah> allSekolah = findAll();
        System.out.println("🗺️ Creating mapping for " + allSekolah.size() + " schools");
        
        // Debug: print semua sekolah
        if (allSekolah.isEmpty()) {
            System.out.println("⚠️ WARNING: No schools found in repository!");
        } else {
            System.out.println("📋 Schools found:");
            allSekolah.forEach(sekolah -> 
                System.out.println("   - " + sekolah.getId() + ": " + sekolah.getNama() + " (" + sekolah.getTingkat() + ")"));
        }
        
        Map<String, String> mapping = new HashMap<>();
        for (Sekolah sekolah : allSekolah) {
            mapping.put(sekolah.getId(), sekolah.getNama());
        }
        
        System.out.println("✅ Mapping created with " + mapping.size() + " entries");
        return mapping;
    }

    // 🔥 METHOD BARU UNTUK DEBUGGING
    public void debugPrintAllSekolah() {
        List<Sekolah> allSekolah = findAll();
        System.out.println("=== DEBUG SEKOLAH DATA ===");
        System.out.println("Total schools: " + allSekolah.size());
        
        if (allSekolah.isEmpty()) {
            System.out.println("❌ NO SCHOOLS FOUND!");
            System.out.println("💡 Check: " + SEKOLAH_FILE_PATH);
        } else {
            allSekolah.forEach(sekolah -> {
                System.out.println("ID: " + sekolah.getId() + 
                                 ", Nama: " + sekolah.getNama() + 
                                 ", Alamat: " + sekolah.getAlamat() + 
                                 ", Tingkat: " + sekolah.getTingkat() +
                                 ", Aktif: " + sekolah.isAktif());
            });
        }
        System.out.println("=== END DEBUG ===");
    }
}