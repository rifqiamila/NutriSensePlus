package com.nutrisense.models.laporan;

import java.time.LocalDate;
import java.util.Map;

import com.nutrisense.models.makanan.GiziResult;

public class LaporanGizi {
    private String id;
    private String dapurId;
    private String sekolahId;
    private LocalDate periodeStart;
    private LocalDate periodeEnd;
    private Map<LocalDate, GiziResult> dataHarian; // Gizi per hari dalam periode
    private GiziResult rataRata;
    private Map<String, Integer> statistikStatus; // Count HIJAU, KUNING, MERAH
    private String jsonData; // Untuk export
    private LocalDate createdAt;

    // Constructors
    public LaporanGizi() {}

    public LaporanGizi(String id, String dapurId, String sekolahId, 
                      LocalDate periodeStart, LocalDate periodeEnd) {
        this.id = id;
        this.dapurId = dapurId;
        this.sekolahId = sekolahId;
        this.periodeStart = periodeStart;
        this.periodeEnd = periodeEnd;
        this.createdAt = LocalDate.now();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDapurId() { return dapurId; }
    public void setDapurId(String dapurId) { this.dapurId = dapurId; }

    public String getSekolahId() { return sekolahId; }
    public void setSekolahId(String sekolahId) { this.sekolahId = sekolahId; }

    public LocalDate getPeriodeStart() { return periodeStart; }
    public void setPeriodeStart(LocalDate periodeStart) { this.periodeStart = periodeStart; }

    public LocalDate getPeriodeEnd() { return periodeEnd; }
    public void setPeriodeEnd(LocalDate periodeEnd) { this.periodeEnd = periodeEnd; }

    public Map<LocalDate, GiziResult> getDataHarian() { return dataHarian; }
    public void setDataHarian(Map<LocalDate, GiziResult> dataHarian) { this.dataHarian = dataHarian; }

    public GiziResult getRataRata() { return rataRata; }
    public void setRataRata(GiziResult rataRata) { this.rataRata = rataRata; }

    public Map<String, Integer> getStatistikStatus() { return statistikStatus; }
    public void setStatistikStatus(Map<String, Integer> statistikStatus) { this.statistikStatus = statistikStatus; }

    public String getJsonData() { return jsonData; }
    public void setJsonData(String jsonData) { this.jsonData = jsonData; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    // Helper methods
    public int getTotalHari() {
        return dataHarian != null ? dataHarian.size() : 0;
    }

    public double getPersentaseStatus(String status) {
        if (statistikStatus == null || statistikStatus.isEmpty()) return 0;
        int total = statistikStatus.values().stream().mapToInt(Integer::intValue).sum();
        int count = statistikStatus.getOrDefault(status, 0);
        return total > 0 ? (count * 100.0) / total : 0;
    }

    @Override
    public String toString() {
        return "LaporanGizi{" +
                "id='" + id + '\'' +
                ", dapurId='" + dapurId + '\'' +
                ", sekolahId='" + sekolahId + '\'' +
                ", periode=" + periodeStart + " to " + periodeEnd +
                ", totalHari=" + getTotalHari() +
                '}';
    }
}