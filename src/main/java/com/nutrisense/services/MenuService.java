package com.nutrisense.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.Makanan;
import com.nutrisense.models.makanan.MenuMBG;
import com.nutrisense.models.report.IndicatorStatus;
import com.nutrisense.models.user.Sekolah;
import com.nutrisense.repositories.MakananRepository;
import com.nutrisense.repositories.MenuRepository;
import com.nutrisense.repositories.SekolahRepository;

public class MenuService {
    private final MenuRepository menuRepo;
    private final MakananRepository makananRepo;
    private final SekolahRepository sekolahRepo;
    private final GiziBalanceTracker giziTracker;
    private final FoodSafetyAlert safetyAlert;

    public MenuService() {
        this.menuRepo = new MenuRepository();
        this.makananRepo = new MakananRepository();
        this.sekolahRepo = new SekolahRepository();
        this.giziTracker = new GiziBalanceTracker();
        this.safetyAlert = new FoodSafetyAlert();
    }

    // For User Umum - Get published menus
    public List<MenuMBG> getPublishedMenus() {
        return menuRepo.findPublishedMenus().stream()
                .sorted((m1, m2) -> m2.getTanggal().compareTo(m1.getTanggal())) // Newest first
                .collect(Collectors.toList());
    }

    public List<MenuMBG> getPublishedMenusBySekolah(String sekolahId) {
        return menuRepo.findBySekolah(sekolahId).stream()
                .filter(MenuMBG::isPublished)
                .collect(Collectors.toList());
    }

    public List<MenuMBG> getPublishedMenusByDapur(String dapurId) {
        return menuRepo.findByDapur(dapurId).stream()
                .filter(MenuMBG::isPublished)
                .collect(Collectors.toList());
    }

    // For Dapur MBG - Menu management
    public MenuMBG createMenu(String namaMenu, String dapurId, String sekolahId, LocalDate tanggal) {
        String menuId = "MENU" + System.currentTimeMillis();
        MenuMBG menu = new MenuMBG(menuId, namaMenu, dapurId, sekolahId, tanggal);
        menuRepo.save(menu);
        return menu;
    }

    public boolean addMakananToMenu(String menuId, String namaMakanan, double beratGram) {
        MenuMBG menu = menuRepo.findById(menuId);
        if (menu == null) return false;

        Makanan makanan = makananRepo.findByNama(namaMakanan);
        if (makanan == null) return false;

        menu.tambahMakanan(makanan, beratGram);
        menuRepo.save(menu);
        return true;
    }

    public boolean publishMenu(String menuId) {
        MenuMBG menu = menuRepo.findById(menuId);
        if (menu == null) return false;

        // Check gizi balance before publishing
        IndicatorStatus status = giziTracker.evaluate(menu.getTotalGizi());
        if (!safetyAlert.isAllowedToServe(status)) {
            return false; // Cannot publish if status is MERAH
        }

        menu.setPublished(true);
        menuRepo.save(menu);
        return true;
    }

    public String getFoodSafetyWarning(MenuMBG menu) {
        IndicatorStatus status = giziTracker.evaluate(menu.getTotalGizi());
        return safetyAlert.getWarningMessage(status);
    }

    public Map<String, Object> analyzeMenuComprehensive(MenuMBG menu) {
        Map<String, Object> analysis = new HashMap<>();
        
        IndicatorStatus status = giziTracker.evaluate(menu.getTotalGizi());
        analysis.put("status", status);
        analysis.put("safetyMessage", safetyAlert.getWarningMessage(status));
        analysis.put("isSafeToServe", safetyAlert.isAllowedToServe(status));
        analysis.put("giziDetails", menu.getTotalGizi());
        analysis.put("analysisMessage", getGiziAnalysisMessage(menu));
        
        return analysis;
    }

    // For Siswa - Get school menus
    public List<MenuMBG> getMenusForSiswa(String sekolahId) {
        return menuRepo.findBySekolah(sekolahId).stream()
                .filter(MenuMBG::isPublished)
                .collect(Collectors.toList());
    }

    public MenuMBG getMenuHariIniForSekolah(String sekolahId) {
        return menuRepo.findBySekolah(sekolahId).stream()
                .filter(menu -> menu.getTanggal().equals(LocalDate.now()))
                .filter(MenuMBG::isPublished)
                .findFirst()
                .orElse(null);
    }

    // Analysis
    public IndicatorStatus analyzeMenuGizi(MenuMBG menu) {
        return giziTracker.evaluate(menu.getTotalGizi());
    }

    public String getGiziAnalysisMessage(MenuMBG menu) {
        IndicatorStatus status = analyzeMenuGizi(menu);
        GiziResult gizi = menu.getTotalGizi();
        
        switch (status) {
            case HIJAU:
                return "✅ Menu memiliki gizi yang seimbang";
            case KUNING:
                return "⚠️ Menu perlu perbaikan: " + getGiziKekurangan(gizi);
            case MERAH:
                return "❌ Menu tidak memenuhi standar gizi: " + getGiziKekurangan(gizi);
            default:
                return "Status gizi tidak diketahui";
        }
    }

    private String getGiziKekurangan(GiziResult gizi) {
        StringBuilder issues = new StringBuilder();
        
        if (gizi.getTotalKalori() < 600) {
            issues.append("Kalori rendah (").append(gizi.getTotalKalori()).append(" kkal), ");
        }
        if (gizi.getTotalProtein() < 23) {
            issues.append("Protein kurang (").append(gizi.getTotalProtein()).append("g), ");
        }
        if (gizi.getTotalLemak() > 25) {
            issues.append("Lemak tinggi (").append(gizi.getTotalLemak()).append("g), ");
        }
        
        if (issues.length() > 0) {
            return issues.substring(0, issues.length() - 2); // Remove last comma
        }
        
        return "Tidak ada issue spesifik";
    }

    // Helper methods
    public List<String> getAllMakananNames() {
        return makananRepo.searchMakananNames("");
    }

    public List<String> getAllSekolahNames() {
        return sekolahRepo.findAll().stream()
                .map(Sekolah::getNama)
                .collect(Collectors.toList());
    }
}