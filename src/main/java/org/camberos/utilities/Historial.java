package org.camberos.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Historial {

    private static final String CSV_SEPARATOR = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");


    public static void guardarResultado(Date fecha, String monedaOrigen, String monedaDestino, String cantidad, String precioUnitario, String precioTotal, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            String fechaFormato = DATE_FORMAT.format(fecha);
            String[] data = {fechaFormato, monedaOrigen, monedaDestino, cantidad, precioUnitario, precioTotal};
            String line = String.join(CSV_SEPARATOR, data) + System.lineSeparator();
            writer.append(line);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el resultado en el historial", e);
        }
    }

    public List<String> leerCSV(String ruta) {
        Path path = Paths.get(ruta);
        crearArchivoSiNoExiste(path);
        List<String> lineas = leerLineasDesdeArchivo(ruta);
        Collections.reverse(lineas);
        return lineas;
    }

    private void crearArchivoSiNoExiste(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException("Error al crear el archivo", e);
            }
        }
    }

    private List<String> leerLineasDesdeArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo", e);
        }
        return lineas;
    }
}


