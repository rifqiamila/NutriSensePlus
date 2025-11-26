package com.nutrisense.models.user;

public class Siswa {
    private String nama;
    private String nisn;
    private String sekolah;
    
    public Siswa(String nama, String nisn, String sekolah) {
        this.nama = nama;
        this.nisn = nisn;
        this.sekolah = sekolah;
    }

    public String getNama() {
        return nama;
    }

    public String getNisn() {
        return nisn;
    }

    public String getSekolah() {
        return sekolah;
    }

    public void setSekolah(String sekolah) {
        this.sekolah = sekolah;
    }

    @Override
    public String toString() {
        return "Siswa{'nama='" + nama + "', nisn='" + nisn + "', sekolah='" + sekolah + "'}";
    }
}
