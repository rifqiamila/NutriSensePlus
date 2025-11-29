package com.nutrisense.models.user;

public class Sekolah {
    private String id;
    private String nama;
    private String alamat;
    private String tingkat; // "SD", "SMP", "SMA"
    private boolean aktif;

    public Sekolah(String id, String nama, String alamat, String tingkat) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.tingkat = tingkat;
        this.aktif = true;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getTingkat() { return tingkat; }
    public void setTingkat(String tingkat) { this.tingkat = tingkat; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    @Override
    public String toString() {
        return "Sekolah{" +
                "id='" + id + '\'' +
                ", nama='" + nama + '\'' +
                ", alamat='" + alamat + '\'' +
                ", tingkat='" + tingkat + '\'' +
                ", aktif=" + aktif +
                '}';
    }
}