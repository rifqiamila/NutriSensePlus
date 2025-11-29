package com.nutrisense.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.nutrisense.models.makanan.MenuMBG;

public class MenuRepository extends JsonRepository<MenuMBG> {
    
    private static final String MENU_FILE_PATH = "src/main/resources/data/menus.json";

    public MenuRepository() {
        super(MENU_FILE_PATH, MenuMBG.class);
    }

    // Menu-specific methods
    public List<MenuMBG> findPublishedMenus() {
        return findAll().stream()
                .filter(MenuMBG::isPublished)
                .collect(Collectors.toList());
    }

    public List<MenuMBG> findByDapur(String dapurId) {
        return findAll().stream()
                .filter(menu -> dapurId.equals(menu.getDapurId()))
                .collect(Collectors.toList());
    }

    public List<MenuMBG> findBySekolah(String sekolahId) {
        return findAll().stream()
                .filter(menu -> sekolahId.equals(menu.getSekolahId()))
                .collect(Collectors.toList());
    }

    public List<MenuMBG> findByDate(LocalDate date) {
        return findAll().stream()
                .filter(menu -> date.equals(menu.getTanggal()))
                .collect(Collectors.toList());
    }

    public List<MenuMBG> findByDapurAndDate(String dapurId, LocalDate date) {
        return findAll().stream()
                .filter(menu -> dapurId.equals(menu.getDapurId()) && date.equals(menu.getTanggal()))
                .collect(Collectors.toList());
    }
}