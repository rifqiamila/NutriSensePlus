package com.nutrisense.repositories;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealPlanRepository {

    private final Map<LocalDate, List<String>> database = new HashMap<>();

    public List<String> getMenuForDate(LocalDate date) {
        return database.get(date);
    }

    public void save(LocalDate date, List<String> menu) {
        database.put(date, menu);
    }
}