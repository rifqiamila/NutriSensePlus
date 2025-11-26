package com.nutrisense.models.user;

import java.util.ArrayList;
import java.util.List;

public class DapurMBG {
    private String nama;
    private String password;
    private String username;
    private List<String> penanggungJawab = new ArrayList<>();
    private List<Siswa> daftarSiswa = new ArrayList<>();
    private List<String> daftarSekolah = new ArrayList<>();

    public DapurMBG(String nama, String password, String username) {
        this.nama = nama;
        this.password = password;
        this.username = username;
        this.daftarSiswa = new ArrayList<>();
        this.daftarSekolah = new ArrayList<>();
        this.penanggungJawab = new ArrayList<>();
    }

    public void tambahSiswa(Siswa s) {
        daftarSiswa.add(s);
    }

    public int hitungSiswa() {
        return daftarSiswa.size();
    }

    public void tambahSekolah(String sekolah) {
        daftarSekolah.add(sekolah);
    }

    public void tambahPenanggungJawab(String penanggungJawab) {
        this.penanggungJawab.add(penanggungJawab);
    }

    public void tampilkanSekolah() {
        System.out.println("=== Daftar Sekolah ===");
        for (String sekolah : daftarSekolah) {
            System.out.println(sekolah);
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void tampilkanPenanggungJawab() {
        System.out.println("=== Daftar Penanggung Jawab ===");
        for (String pj : penanggungJawab) {
            System.out.println(pj);
        }
    }

    public void tampilkanSemua() {
        System.out.println("=== Daftar Siswa ===");
        for (Siswa s : daftarSiswa) {
            System.out.println(s);
        }
    }
}
