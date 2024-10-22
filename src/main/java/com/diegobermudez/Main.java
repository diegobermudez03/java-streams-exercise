package com.diegobermudez;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get("datosDivipola.csv"), Charset.forName("ISO-8859-1"));
        final List<Row> rows = lines.stream().skip(1).map(Main::lineToRow).peek(System.out::println).toList();


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