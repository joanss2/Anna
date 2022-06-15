package com.example.anna.MenuPrincipal.Routes;

import com.google.android.gms.maps.model.LatLng;

public enum Ubications {
    SANT_PERE_UBI("SantPeredePonts", new LatLng(41.91640526709107, 1.1965857)),
    SANTA_MARIA_UBI("SantaMaria", new LatLng(41.91777693963667, 1.1878681933369661)),
    CONDIS_UBI("Condis", new LatLng(41.91621297223844, 1.1833116654458429)),
    HOMILIES_UBI("Homilies", new LatLng(42.211759848001904, 1.3289212885394481)),
    NATURLANDIA_UBI("Naturlandia", new LatLng(42.44206313154928, 1.5020963973793082));


    private final String key;
    private final LatLng value;

    Ubications(String key, LatLng value) {
        this.key = key;
        this.value = value;
    }
    public LatLng getValue() {
        return value;
    }
}
