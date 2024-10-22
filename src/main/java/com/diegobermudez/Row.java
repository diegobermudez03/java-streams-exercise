package com.diegobermudez;

public class Row {
    final int codDepto;
    final String nombreDepto;
    final int codMunicipio;
    final String nombreMun;
    final double superficie;
    final int urbana;
    final int rural;


    public Row(int codDepto, String nombreDepto, int codMunicipio, String nombreMun, double superficie, int urbana, int rural){
        this.codDepto = codDepto;
        this.nombreDepto = nombreDepto;
        this.codMunicipio = codMunicipio;
        this.nombreMun =nombreMun;
        this.superficie = superficie;
        this.urbana = urbana;
        this.rural = rural;
    }


    @Override
    public String toString() {
        return codDepto + " " + nombreDepto + " " + codMunicipio + " " + nombreMun + " " + superficie;
    }
}

