package com.nutrisense.models.report;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.nutrisense.models.makanan.GiziResult;
import com.nutrisense.models.makanan.MenuMBG;

public class Report {

    private MenuMBG menu;
    private GiziResult giziResult;
    private IndicatorStatus indicatorStatus;
    private String timestamp;

    public Report(MenuMBG menu, GiziResult giziResult, IndicatorStatus indicatorStatus) {
        this.menu = menu;
        this.giziResult = giziResult;
        this.indicatorStatus = indicatorStatus;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public MenuMBG getMenu() {
        return menu;
    }

    public GiziResult getGiziResult() {
        return giziResult;
    }

    public IndicatorStatus getIndicatorStatus() {
        return indicatorStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
