package com.nutrisense.models.makanan;

import java.util.ArrayList;
import java.util.List;

public class MenuMBG {

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
}
