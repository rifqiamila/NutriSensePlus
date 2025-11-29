package com.nutrisense.models.user;

public class Siswa extends User {
    private String nisn;
    private String sekolahId;
    private String namaLengkap;

    public Siswa() {
        super();
        this.role = UserRole.SISWA;
        this.nisn = "";
        this.sekolahId = "";
        this.namaLengkap = "";
    }

    public Siswa(String nisn, String sekolahId, String nama) {
        super();
        this.role = UserRole.SISWA;
        this.nisn = nisn;
        this.sekolahId = sekolahId;
        this.namaLengkap = namaLengkap;
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