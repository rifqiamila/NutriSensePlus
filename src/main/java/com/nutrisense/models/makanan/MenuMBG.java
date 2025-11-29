package com.nutrisense.models.makanan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuMBG {
    private String id;
    private String namaMenu;
    private String dapurId;
    private String sekolahId;
    private LocalDate tanggal;
    private List<MenuMakanan> items = new ArrayList<>();
    private boolean isPublished;
    private GiziResult totalGizi;

    // Constructors
    public MenuMBG() {}

    public MenuMBG(String id, String namaMenu, String dapurId, String sekolahId, LocalDate tanggal) {
        this.id = id;
        this.namaMenu = namaMenu;
        this.dapurId = dapurId;
        this.sekolahId = sekolahId;
        this.tanggal = tanggal;
        this.isPublished = false;
    }

    // Methods
    public void tambahMakanan(Makanan makanan, double beratGram) {
        items.add(new MenuMakanan(makanan, beratGram));
        hitungTotalGizi();
    }

    public void tambahMenuMakanan(MenuMakanan menuMakanan) {
        items.add(menuMakanan);
        hitungTotalGizi();
    }

    private void hitungTotalGizi() {
        double totalKalori = 0;
        double totalProtein = 0;
        double totalKarbohidrat = 0;
        double totalLemak = 0;
        double totalSerat = 0;

        for (MenuMakanan item : items) {
            totalKalori += item.getKalori();
            totalProtein += item.getProtein();
            totalKarbohidrat += item.getKarbohidrat();
            totalLemak += item.getLemak();
            totalSerat += item.getSerat();
        }

        this.totalGizi = new GiziResult(totalKalori, totalProtein, totalKarbohidrat, totalLemak, totalSerat);
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNamaMenu() { return namaMenu; }
    public void setNamaMenu(String namaMenu) { this.namaMenu = namaMenu; }

    public String getDapurId() { return dapurId; }
    public void setDapurId(String dapurId) { this.dapurId = dapurId; }

    public String getSekolahId() { return sekolahId; }
    public void setSekolahId(String sekolahId) { this.sekolahId = sekolahId; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public List<MenuMakanan> getItems() { return items; }
    public void setItems(List<MenuMakanan> items) { 
        this.items = items; 
        hitungTotalGizi();
    }

    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean published) { isPublished = published; }

    public GiziResult getTotalGizi() { return totalGizi; }

    @Override
    public String toString() {
        return "MenuMBG{" +
                "id='" + id + '\'' +
                ", namaMenu='" + namaMenu + '\'' +
                ", dapurId='" + dapurId + '\'' +
                ", sekolahId='" + sekolahId + '\'' +
                ", tanggal=" + tanggal +
                ", items=" + items.size() +
                ", isPublished=" + isPublished +
                '}';
    }
}