package com.diegobermudez;

public record Department(
        int id,
        String departamento,
        double densidadTotal,
        double densidadUrbana,
        double densidadRural,
        double porPobUrbana,
        double porPobRural,
        double areaPromedio,
        String munMasGrande,
        String munMasChico,
        String munMayorDens,
        String munMenorDens
) {

}
