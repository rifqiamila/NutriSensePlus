package com.nutrisense.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nutrisense.models.laporan.Feedback;
import com.nutrisense.models.laporan.LaporanGizi;
import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.MenuMBG;
import com.nutrisense.models.report.IndicatorStatus;
import com.nutrisense.repositories.FeedbackRepository;
import com.nutrisense.repositories.LaporanRepository;
import com.nutrisense.repositories.MenuRepository;

public class ReportService {
    private final LaporanRepository laporanRepo;
    private final FeedbackRepository feedbackRepo;
    private final MenuRepository menuRepo;
    private final GiziBalanceTracker giziTracker;

    public ReportService() {
        this.laporanRepo = new LaporanRepository();
        this.feedbackRepo = new FeedbackRepository();
        this.menuRepo = new MenuRepository();
        this.giziTracker = new GiziBalanceTracker();
    }

    // Laporan Generation
    public LaporanGizi generateLaporanMingguan(String dapurId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        
        // Check if report already exists
        LaporanGizi existing = laporanRepo.findLaporanMingguan(dapurId, startDate);
        if (existing != null) {
            return existing;
        }

        // Get menus for the week
        List<MenuMBG> menus = menuRepo.findByDapur(dapurId).stream()
                .filter(menu -> !menu.getTanggal().isBefore(startDate) && !menu.getTanggal().isAfter(endDate))
                .filter(MenuMBG::isPublished)
                .collect(Collectors.toList());

        // Calculate statistics
        Map<LocalDate, GiziResult> dataHarian = new HashMap<>();
        Map<String, Integer> statistikStatus = new HashMap<>();
        
        for (MenuMBG menu : menus) {
            dataHarian.put(menu.getTanggal(), menu.getTotalGizi());
            
            IndicatorStatus status = giziTracker.evaluate(menu.getTotalGizi());
            statistikStatus.merge(status.name(), 1, Integer::sum);
        }

        // Calculate averages
        GiziResult rataRata = calculateRataRata(dataHarian.values());

        // Create report
        String reportId = "RPT" + System.currentTimeMillis();
        LaporanGizi laporan = new LaporanGizi(reportId, dapurId, getSekolahIdFromDapur(dapurId), startDate, endDate);
        laporan.setDataHarian(dataHarian);
        laporan.setRataRata(rataRata);
        laporan.setStatistikStatus(statistikStatus);
        laporan.setJsonData(convertToJsonString(laporan));

        laporanRepo.save(laporan);
        return laporan;
    }

    // Feedback Management
    public boolean submitFeedback(String siswaId, String menuId, String dapurId, int rating, String komentar) {
        String feedbackId = "FB" + System.currentTimeMillis();
        Feedback feedback = new Feedback(feedbackId, siswaId, menuId, dapurId, rating, komentar);
        feedbackRepo.save(feedback);
        return true;
    }

    public List<Feedback> getFeedbackForDapur(String dapurId) {
        return feedbackRepo.findByDapur(dapurId).stream()
                .filter(Feedback::isActive)
                .collect(Collectors.toList());
    }

    public List<Feedback> getFeedbackForMenu(String menuId) {
        return feedbackRepo.findByMenu(menuId).stream()
                .filter(Feedback::isActive)
                .collect(Collectors.toList());
    }

    public double getAverageRatingForDapur(String dapurId) {
        return feedbackRepo.getAverageRatingByDapur(dapurId);
    }

    public double getAverageRatingForMenu(String menuId) {
        return feedbackRepo.getAverageRatingByMenu(menuId);
    }

    // Analytics
    public Map<String, Object> getDapurAnalytics(String dapurId) {
        Map<String, Object> analytics = new HashMap<>();
        
        // Rating analytics
        analytics.put("averageRating", getAverageRatingForDapur(dapurId));
        analytics.put("totalFeedback", getFeedbackForDapur(dapurId).size());
        
        // Menu analytics
        List<MenuMBG> menus = menuRepo.findByDapur(dapurId);
        analytics.put("totalMenus", menus.size());
        analytics.put("publishedMenus", menus.stream().filter(MenuMBG::isPublished).count());
        
        // Gizi analytics
        long hijauCount = menus.stream()
                .filter(menu -> giziTracker.evaluate(menu.getTotalGizi()) == IndicatorStatus.HIJAU)
                .count();
        analytics.put("giziSeimbangPercentage", menus.isEmpty() ? 0 : (hijauCount * 100.0) / menus.size());
        
        return analytics;
    }

    // Helper methods
    private GiziResult calculateRataRata(java.util.Collection<GiziResult> giziResults) {
        if (giziResults.isEmpty()) {
            return new GiziResult(0, 0, 0, 0, 0);
        }

        double totalKalori = 0, totalProtein = 0, totalKarbo = 0, totalLemak = 0, totalSerat = 0;
        int count = giziResults.size();

        for (GiziResult gizi : giziResults) {
            totalKalori += gizi.getTotalKalori();
            totalProtein += gizi.getTotalProtein();
            totalKarbo += gizi.getTotalKarbohidrat();
            totalLemak += gizi.getTotalLemak();
            totalSerat += gizi.getTotalSerat();
        }

        return new GiziResult(
            totalKalori / count,
            totalProtein / count,
            totalKarbo / count,
            totalLemak / count,
            totalSerat / count
        );
    }

    private String getSekolahIdFromDapur(String dapurId) {
        // This would need integration with UserService
        // For now, return first sekolah or empty
        return "";
    }

    private String convertToJsonString(LaporanGizi laporan) {
        // Simple JSON conversion for export
        return String.format(
            "{\"id\":\"%s\",\"dapurId\":\"%s\",\"periode\":\"%s to %s\",\"totalHari\":%d}",
            laporan.getId(), laporan.getDapurId(), 
            laporan.getPeriodeStart(), laporan.getPeriodeEnd(),
            laporan.getTotalHari()
        );
    }
}