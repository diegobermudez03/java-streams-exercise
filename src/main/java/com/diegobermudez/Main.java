package com.diegobermudez;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get("datosDivipola.csv"), StandardCharsets.ISO_8859_1);

        final List<Row> rows = lines.stream()
                .skip(1)
                .map(Main::lineToRow)
                .toList();

        final Map<Integer, List<Row>> departments = rows.stream()
                .collect(Collectors.groupingBy(Row::codDepto));


        final List<Department> depStatitics = departments.entrySet().stream().map((entry)->{
            final int deptId = entry.getKey();
            final List<Row> municipios = entry.getValue();

            final double superficie = municipios.stream()
                    .map(Row::superficie)
                    .reduce(0d,(previous, current)->previous+current);

            final int pbRural = municipios.stream()
                    .map(Row::rural)
                    .reduce(0, (previous, current)->previous+current);

            final int pbUrbana = municipios.stream()
                    .map(Row::urbana)
                    .reduce(0, (previous, current)->previous+current);

            final double pbTotal = pbRural + pbUrbana;
            final double areaPromedio = superficie / municipios.stream().count();

            final String munMasGrande = municipios.stream()
                    .max(Comparator.comparing(Row::superficie))
                    .map(Row::nombreMun)
                    .orElse("No se sabe");

            final String munMasChico = municipios.stream()
                    .min(Comparator.comparing(Row::superficie))
                    .map(Row::nombreMun)
                    .orElse("No se sabe");

            final String munMayorDens = municipios.stream()
                    .max(Comparator.comparing(row -> (row.rural() + row.urbana()) / row.superficie()))
                    .map(Row::nombreMun)
                    .orElse("No se sabe");

            final String munMenorDens = municipios.stream()
                    .min(Comparator.comparing(row -> (row.rural() + row.urbana()) / row.superficie()))
                    .map(Row::nombreMun)
                    .orElse("No se sabe");

            return new Department(
                    deptId,
                    municipios.get(0).nombreDepto(),
                    pbTotal / superficie,
                    pbUrbana / superficie,
                    pbRural / superficie,
                    (pbUrbana/pbTotal)*100,
                    (pbRural/pbTotal)*100,
                    areaPromedio, munMasGrande, munMasChico, munMayorDens, munMenorDens
            );
        }).toList();


        depStatitics.forEach((dept)->{
            System.out.printf("%s ID: %d, Densidad total: %.2f, Densidad Urbana: %.2f, Densidad Rural: %.2f, "
                            + "Porcentaje Poblacion Urbana: %.2f%%, Porcentaje Poblacion Rural: %.2f%%%n",
                    dept.departamento(), dept.id(), dept.densidadTotal(), dept.densidadUrbana(), dept.densidadRural(),
                    dept.porPobUrbana(), dept.porPobRural());

            System.out.printf("    Area promedio: %.2f, Municipio más grande: %s, Municipio más chico: %s, "
                            + "Municipio mayor densidad: %s, Municipio menor densidad: %s%n",
                    dept.areaPromedio(), dept.munMasGrande(), dept.munMasChico(),
                    dept.munMayorDens(), dept.munMenorDens());
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