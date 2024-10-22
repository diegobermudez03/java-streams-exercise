package com.diegobermudez;

public record Row(
        int codDepto,
        String nombreDepto,
        int codMunicipio,
        String nombreMun,
        double superficie,
        int urbana,
        int rural
) {
    @Override
    public String toString() {
        return codDepto + " " + nombreDepto + " " + codMunicipio + " " + nombreMun + " " + superficie;
    }
}

