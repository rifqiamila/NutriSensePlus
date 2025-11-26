package com.nutrisense.models.report;


import java.util.List;

public class IndicatorStatus {

    public enum IndicatorColor {
        HIJAU,
        KUNING,
        MERAH
    }

    private IndicatorColor color;
    private List<String> notes;
    private boolean allowedToProceed;

    public IndicatorStatus(IndicatorColor color, List<String> notes) {
        this.color = color;
        this.notes = notes;
        this.allowedToProceed = (color != IndicatorColor.MERAH);
    }

    public IndicatorColor getColor() {
        return color;
    }

    public List<String> getNotes() {
        return notes;
    }

    public boolean isAllowedToProceed() {
        return allowedToProceed;
    }

    @Override
    public String toString() {
        return "IndicatorStatus{" +
                "color=" + color +
                ", notes=" + notes +
                ", allowedToProceed=" + allowedToProceed +
                '}';
    }
}
