package com.nutrisense.repositories;

import com.nutrisense.models.makanan.MenuMBG;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MenuRepository {

    private static final String FILE_PATH = "src/main/resources/data/menus.json";
    private List<MenuMBG> menus;

    public MenuRepository() {
        Type listType = new TypeToken<List<MenuMBG>>(){}.getType();
        this.menus = DatabaseJSON.loadList(FILE_PATH, listType);
    }

    public List<MenuMBG> findAll() {
        return menus;
    }

    public MenuMBG findByName(String name) {
        return menus.stream()
                .filter(m -> m.getNamaMenu().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void save(MenuMBG menu) {
        menus.add(menu);
        DatabaseJSON.saveList(FILE_PATH, menus);
    }
}
