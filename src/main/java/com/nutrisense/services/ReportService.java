package com.nutrisense.services;

import java.time.LocalDate;

import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.MenuMBG;
import com.nutrisense.models.report.IndicatorStatus;
import com.nutrisense.models.report.Report;
import com.nutrisense.repositories.ReportRepository;

public class ReportService {

    private final GiziAnalyzer analyzer = new GiziAnalyzer();
    private final GiziBalanceTracker balance = new GiziBalanceTracker();
    private final SmartRecommendationEngine engine = new SmartRecommendationEngine();
    private final FoodSafetyAlert alert = new FoodSafetyAlert();

    private final ReportRepository repo = new ReportRepository();

    public Report generateReport(String username, MenuMBG menu) {

        // 1. hitung gizi
        GiziResult gizi = analyzer.analyzeMenu(menu);

        // 2. dapatkan status (Hijau/Kuning/Merah)
        IndicatorStatus status = balance.evaluate(gizi);

        // 3. rekomendasi
        var recommendations = engine.recommend(gizi);

        // 4. peringatan
        var warning = alert.getWarningMessage(status);

        // 5. buat report
        Report report = new Report(
                username,
                menu.getNamaMenu(),
                gizi,
                status,
                recommendations,
                warning
        );

        // 6. simpan ke repository
        repo.save(report);

        return report;
    }
}
