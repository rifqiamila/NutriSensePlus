package com.nutrisense.models.user;

import java.util.ArrayList;
import java.util.List;

public class DapurMBG extends User {
    private String namaDapur;
    private List<String> penanggungJawab;
    private List<String> sekolahIds;

    public DapurMBG() {
        super();
        this.role = UserRole.DAPUR_MBG;
        this.namaDapur = "";
        this.penanggungJawab = new ArrayList<>();
        this.sekolahIds = new ArrayList<>();
    }

    public DapurMBG(String namaDapur, List<String> sekolahIds) {
        super();
        this.role = UserRole.DAPUR_MBG;
        this.namaDapur = namaDapur;
        this.sekolahIds = sekolahIds;
        this.penanggungJawab = new ArrayList<>();
    }

    // Getters & Setters
    public String getNamaDapur() { return namaDapur; }
    public void setNamaDapur(String namaDapur) { this.namaDapur = namaDapur; }

    public List<String> getPenanggungJawab() { return penanggungJawab; }
    public void setPenanggungJawab(List<String> penanggungJawab) { 
        this.penanggungJawab = penanggungJawab; 
    }

    public List<String> getSekolahIds() { return sekolahIds; }
    public void setSekolahIds(List<String> sekolahIds) { 
        this.sekolahIds = sekolahIds; 
    }

    // Helper methods
    public void addSekolah(String sekolahId) {
        if (!this.sekolahIds.contains(sekolahId)) {
            this.sekolahIds.add(sekolahId);
        }
    }

    public void addPenanggungJawab(String namaPJ) {
        if (!this.penanggungJawab.contains(namaPJ)) {
            this.penanggungJawab.add(namaPJ);
        }
    }

    @Override
    public String toString() {
        return "DapurMBG{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", namaDapur='" + namaDapur + '\'' +
                ", sekolahIds=" + sekolahIds +
                ", penanggungJawab=" + penanggungJawab +
                ", isActive=" + isActive +
                '}';
    }
}