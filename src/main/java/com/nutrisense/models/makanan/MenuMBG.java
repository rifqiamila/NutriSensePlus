package com.nutrisense.models.makanan;

import java.util.ArrayList;
import java.util.List;

public class MenuMBG {

    private String namaMenu;

    private List<Makanan> daftarMakanan = new ArrayList<>();

    public void tambahMakanan(Makanan m) {
        daftarMakanan.add(m);
    }

    public void tampilkanMenu() {
        System.out.println("=== MENU MBG ===");
        for (Makanan m : daftarMakanan) {
            System.out.println("- " + m);
        }
    }

    public List<Makanan> getDaftarMakanan() {
        return daftarMakanan;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }
}
