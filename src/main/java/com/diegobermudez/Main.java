package com.diegobermudez;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get("datosDivipola.csv"), Charset.forName("ISO-8859-1"));

        final List<Row> rows = lines.stream()
                .skip(1)
                .map(Main::lineToRow)
                .toList();

        final Map<Integer, List<Row>> departments = rows.stream()
                .collect(Collectors.groupingBy(Row::codDepto));

        final List<Department> deptStatitics = new LinkedList<>();

        departments.forEach((k, row)->{
            final double superficie = row.stream()
                    .map(Row::superficie)
                    .reduce(0d,(previous, current)->previous+current);

            final int pbRural = row.stream()
                    .map(Row::rural)
                    .reduce(0, (previous, current)->previous+current);

            final int pbUrbana = row.stream()
                    .map(Row::urbana)
                    .reduce(0, (previous, current)->previous+current);

            final double pbTotal = pbRural + pbUrbana;
            final double densidadTotal = pbTotal / superficie;
            final double densidadUrbana = pbUrbana / superficie;
            final double densidadRural = pbRural / superficie;
            final double porcPobUrbana = (pbUrbana/pbTotal)*100;
            final double porcPobRural = (pbRural/pbTotal)*100;

            final double areaPromedio = superficie / row.stream().count();

            final String munMasGrande = row.stream()
                    .reduce((previous, next)->next.superficie()>  previous.superficie() ? next : previous)
                    .get().nombreMun();
            final String munMasChico = row.stream()
                    .reduce((previous, next)->next.superficie() < previous.superficie() ? next : previous)
                    .get().nombreMun();

            final String munMayorDens = row.stream()
                    .reduce((previous, next)->{
                        final double densidadNext = (next.rural() + next.urbana()) / next.superficie();
                        final double densidadPrevious = (previous.rural() + previous.urbana()) / previous.superficie();
                        if(densidadNext > densidadPrevious) return next;
                        else return previous;
                    })
                    .get().nombreMun();

            final String munMenorDens = row.stream()
                    .reduce((previous, next)->{
                        final double densidadNext = (next.rural() + next.urbana()) / next.superficie();
                        final double densidadPrevious = (previous.rural() + previous.urbana()) / previous.superficie();
                        if(densidadNext < densidadPrevious) return next;
                        else return previous;
                    })
                    .get().nombreMun();

            deptStatitics.add(
                    new Department(
                            k,
                            densidadTotal,
                            densidadUrbana,
                            densidadRural,
                            porcPobUrbana,
                            porcPobRural,
                            areaPromedio,
                            munMasGrande,
                            munMasChico,
                            munMayorDens,
                            munMenorDens
                    )
            );

        });


        deptStatitics.forEach((dept)->{
            System.out.println("ID: " + dept.id() + " Densidad total: " + dept.densidadTotal() + " Densidad Urbana: " + dept.densidadUrbana()
                    + " Densidad Rural: " + dept.densidadRural() + " Porcentaje poblacion urbana: %" + dept.porPobUrbana() + " Porcentaje poblacion rural: %" + dept.porPobRural()
            );
            System.out.println("    Area promedio: " + dept.areaPromedio() + " Municipio mas grande: " + dept.munMasGrande() + " Municipio mas chico: " + dept.munMasChico()
                    + " Municipio con mayor densidad: " + dept.munMayorDens() + " Municipio menor densidad: " + dept.munMenorDens()
            );
            System.out.println();
        });


    }

    private static Row lineToRow(String line){
        final String[] items = line.split(";");
        return new Row(
                Integer.parseInt(items[0]),
                items[1],
                Integer.parseInt(items[2]),
                items[3].replace("\"", ""),
                Double.parseDouble(items[4]),
                Integer.parseInt(items[5]),
                Integer.parseInt(items[6])
        );
    }
}