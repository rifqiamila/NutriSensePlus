package com.nutrisense.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.nutrisense.models.laporan.LaporanGizi;

public class LaporanRepository extends JsonRepository<LaporanGizi> {
    
    private static final String LAPORAN_FILE_PATH = "src/main/resources/data/laporan.json";
    
    // Implement abstract methods
    @Override
    protected LaporanGizi parseItem(String jsonItem) {
        // Manual parsing implementation for LaporanGizi
        // (akan kita implementasi nanti)
        return null;
    }

    @Override
    protected String convertItemToJson(LaporanGizi item) {
        // Manual serialization for LaporanGizi  
        // (akan kita implementasi nanti)
        return "";
    }

    public LaporanRepository() {
        super(LAPORAN_FILE_PATH, LaporanGizi.class);
    }

    // Laporan-specific methods
    public List<LaporanGizi> findByDapur(String dapurId) {
        return findAll().stream()
                .filter(laporan -> dapurId.equals(laporan.getDapurId()))
                .collect(Collectors.toList());
    }

    public List<LaporanGizi> findBySekolah(String sekolahId) {
        return findAll().stream()
                .filter(laporan -> sekolahId.equals(laporan.getSekolahId()))
                .collect(Collectors.toList());
    }

    public List<LaporanGizi> findByPeriode(LocalDate startDate, LocalDate endDate) {
        return findAll().stream()
                .filter(laporan -> 
                    !laporan.getPeriodeStart().isAfter(endDate) &&
                    !laporan.getPeriodeEnd().isBefore(startDate))
                .collect(Collectors.toList());
    }

    public LaporanGizi findLaporanMingguan(String dapurId, LocalDate date) {
        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return findAll().stream()
                .filter(laporan -> dapurId.equals(laporan.getDapurId()))
                .filter(laporan -> 
                    laporan.getPeriodeStart().equals(startOfWeek) &&
                    laporan.getPeriodeEnd().equals(endOfWeek))
                .findFirst()
                .orElse(null);
    }
}