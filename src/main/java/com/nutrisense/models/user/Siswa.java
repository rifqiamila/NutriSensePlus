package com.nutrisense.models.user;

public class Siswa extends User {

    private String nama;
    private String nisn;
    private String sekolahId;
    private String dapurId;

    public Siswa(String id, String username, String password,
                 String nama, String nisn, String sekolahId, String dapurId) {

        super(id, username, password, "SISWA");
        this.nama = nama;
        this.nisn = nisn;
        this.sekolahId = sekolahId;
        this.dapurId = dapurId;
    }

    // Getters & Setters
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNisn() { return nisn; }
    public void setNisn(String nisn) { this.nisn = nisn; }

    public String getSekolahId() { return sekolahId; }
    public void setSekolahId(String sekolahId) { this.sekolahId = sekolahId; }

    public String getDapurId() { return dapurId; }
    public void setDapurId(String dapurId) { this.dapurId = dapurId; }

    @Override
    public String toString() {
        return "Siswa{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nama='" + nama + '\'' +
                ", nisn='" + nisn + '\'' +
                ", sekolahId='" + sekolahId + '\'' +
                ", dapurId='" + dapurId + '\'' +
                '}';
    }
}