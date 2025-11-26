package com.nutrisense.models.report;

/**
 * Menandakan status keseimbangan gizi harian.
 * 
 * HIJAU  = Seimbang
 * KUNING = Kurang atau sedikit tidak ideal (masih boleh lanjut)
 * MERAH  = Tidak seimbang (tidak boleh lanjut)
 */
public enum IndicatorStatus {
    HIJAU,
    KUNING,
    MERAH
}
