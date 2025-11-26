package com.nutrisense.repositories;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.nutrisense.models.report.Report;

public class ReportRepository {

    private static final String FILE_PATH = "src/main/resources/data/reports.json";
    private List<Report> reports;

    public ReportRepository() {
        Type listType = new TypeToken<List<Report>>(){}.getType();
        this.reports = DatabaseJSON.loadList(FILE_PATH, listType);
    }

    public List<Report> findAll() {
        return reports;
    }

    public List<Report> findByUser(String userId) {
        return reports.stream()
                .filter(r -> r.getUserId().equals(userId))
                .toList();
    }

    public void save(Report report) {
        reports.add(report);
        DatabaseJSON.saveList(FILE_PATH, reports);
    }
}