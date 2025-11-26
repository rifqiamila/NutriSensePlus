package com.nutrisense.models.report;

import java.time.LocalDate;
import java.util.List;

import com.nutrisense.models.makanan.GiziResult;

/**
 * Menyimpan laporan gizi harian berdasarkan menu yang dipilih user.
 * Termasuk status, catatan kekurangan gizi, dan izin melanjutkan proses.
 */
public class Report {

    private String userId;
    private LocalDate tanggal;

    private GiziResult giziResult;        // total gizi harian
    private IndicatorStatus status;       // HIJAU / KUNING / MERAH

    private List<String> notes;           // penjelasan apa yang kurang/lebih
    private boolean allowedToProceed;     // false jika status = MERAH

    public Report(String userId,
                  GiziResult giziResult,
                  IndicatorStatus status,
                  List<String> notes) {

        this.userId = userId;
        this.tanggal = LocalDate.now();

        this.giziResult = giziResult;
        this.status = status;

        this.notes = notes;
        this.allowedToProceed = (status != IndicatorStatus.MERAH);
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public GiziResult getGiziResult() {
        return giziResult;
    }

    public IndicatorStatus getStatus() {
        return status;
    }

    public List<String> getNotes() {
        return notes;
    }

    public boolean isAllowedToProceed() {
        return allowedToProceed;
    }

    @Override
    public String toString() {
        return "Report{" +
                "userId='" + userId + '\'' +
                ", tanggal=" + tanggal +
                ", giziResult=" + giziResult +
                ", status=" + status +
                ", notes=" + notes +
                ", allowedToProceed=" + allowedToProceed +
                '}';
    }
}