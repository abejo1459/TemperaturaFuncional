package servicios;

import modelos.TemperaturaCiudad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemperaturasServicio {

    // 1. Cargar datos desde el CSV usando Streams
    public static List<TemperaturaCiudad> getDatos(String nombreArchivo) {
        // En el PDF el formato es dd/MM/yyyy (ej. 01/01/2024)
        var formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            Stream<String> lineasArchivo = Files.lines(Paths.get(nombreArchivo));
            return lineasArchivo.skip(1) // Saltar el encabezado
                    .map(linea -> linea.split(","))
                    .map(textos -> new TemperaturaCiudad(
                            textos[0].trim(),
                            LocalDate.parse(textos[1].trim(), formatoFecha),
                            Double.parseDouble(textos[2].trim())
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    // 2. Obtener promedios por ciudad en un rango de fechas
    public static Map<String, Double> getPromediosPorCiudad(List<TemperaturaCiudad> datos, LocalDate desde, LocalDate hasta) {
        return datos.stream()
                .filter(t -> !t.getFecha().isBefore(desde) && !t.getFecha().isAfter(hasta))
                .collect(Collectors.groupingBy(
                        TemperaturaCiudad::getCiudad,
                        Collectors.averagingDouble(TemperaturaCiudad::getTemperatura)
                ));
    }

    // 3. Ciudad más calurosa en una fecha específica
    public static Optional<TemperaturaCiudad> getMasCalurosa(List<TemperaturaCiudad> datos, LocalDate fecha) {
        return datos.stream()
                .filter(t -> t.getFecha().equals(fecha))
                .max(Comparator.comparingDouble(TemperaturaCiudad::getTemperatura));
    }

    // 4. Ciudad menos calurosa en una fecha específica
    public static Optional<TemperaturaCiudad> getMenosCalurosa(List<TemperaturaCiudad> datos, LocalDate fecha) {
        return datos.stream()
                .filter(t -> t.getFecha().equals(fecha))
                .min(Comparator.comparingDouble(TemperaturaCiudad::getTemperatura));
    }
}