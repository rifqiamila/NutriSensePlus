package com.nutrisense.models.user;

public class Siswa extends User {
    private String nisn;
    private String namaLengkap;
    private String sekolahId;

    public Siswa(String id, String username, String password, 
                 String nisn, String namaLengkap, String sekolahId) {
        super(id, username, password, UserRole.SISWA);
        this.nisn = nisn;
        this.namaLengkap = namaLengkap;
        this.sekolahId = sekolahId;
    }

    // Getters & Setters
    public String getNisn() { return nisn; }
    public void setNisn(String nisn) { this.nisn = nisn; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getSekolahId() { return sekolahId; }
    public void setSekolahId(String sekolahId) { this.sekolahId = sekolahId; }

    @Override
    public String toString() {
        return "Siswa{" +
                "id='" + id + '\'' +
                ", nisn='" + nisn + '\'' +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", sekolahId='" + sekolahId + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}