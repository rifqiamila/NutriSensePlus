package com.nutrisense.repositories;

import java.util.List;
import java.util.stream.Collectors;

import com.nutrisense.models.user.Sekolah;

public class SekolahRepository extends JsonRepository<Sekolah> {
    
    private static final String SEKOLAH_FILE_PATH = "src/main/resources/data/sekolah.json";

    public SekolahRepository() {
        super(SEKOLAH_FILE_PATH, Sekolah.class);
    }

    // Sekolah-specific methods
    public List<Sekolah> findAktif() {
        return findAll().stream()
                .filter(Sekolah::isAktif)
                .collect(Collectors.toList());
    }

    public List<Sekolah> findByTingkat(String tingkat) {
        return findAll().stream()
                .filter(sekolah -> tingkat.equals(sekolah.getTingkat()))
                .collect(Collectors.toList());
    }

    public List<String> getAllNamaSekolah() {
        return findAll().stream()
                .map(Sekolah::getNama)
                .collect(Collectors.toList());
    }
}