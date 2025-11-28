package com.nutrisense.models.sekolah;

public class Sekolah {
    private String sekolahId;
    private String namaSekolah;
    private String alamat;

    public Sekolah(String sekolahId, String namaSekolah, String alamat) {
        this.sekolahId = sekolahId;
        this.namaSekolah = namaSekolah;
        this.alamat = alamat;
    }

    // Getters & Setters
    public String getSekolahId() { return sekolahId; }
    public void setSekolahId(String sekolahId) { this.sekolahId = sekolahId; }

    public String getNamaSekolah() { return namaSekolah; }
    public void setNamaSekolah(String namaSekolah) { this.namaSekolah = namaSekolah; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public String toString() {
        return "Sekolah{" +
                "sekolahId='" + sekolahId + '\'' +
                ", namaSekolah='" + namaSekolah + '\'' +
                ", alamat='" + alamat + '\'' +
                '}';
    }
}