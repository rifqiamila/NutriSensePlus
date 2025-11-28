package com.nutrisense.models.user;

import java.util.List;

public class DapurMBG extends User {

    private String namaDapur;
    private List<String> penanggungJawab;
    private List<String> sekolahIds; // hanya ID sekolah

    public DapurMBG(String id, String username, String password,
                    String namaDapur, List<String> penanggungJawab,
                    List<String> sekolahIds) {

        super(id, username, password, "DAPUR");
        this.namaDapur = namaDapur;
        this.penanggungJawab = penanggungJawab;
        this.sekolahIds = sekolahIds;
    }

    public String getNamaDapur() { return namaDapur; }
    public void setNamaDapur(String namaDapur) { this.namaDapur = namaDapur; }

    public List<String> getPenanggungJawab() { return penanggungJawab; }
    public void setPenanggungJawab(List<String> penanggungJawab) { this.penanggungJawab = penanggungJawab; }

    public List<String> getSekolahIds() { return sekolahIds; }
    public void setSekolahIds(List<String> sekolahIds) { this.sekolahIds = sekolahIds; }

    @Override
    public String toString() {
        return "DapurMBG{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", namaDapur='" + namaDapur + '\'' +
                ", sekolahIds=" + sekolahIds +
                ", penanggungJawab=" + penanggungJawab +
                '}';
    }
}
