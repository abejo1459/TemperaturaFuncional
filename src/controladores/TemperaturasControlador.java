package controladores;

import modelos.TemperaturaCiudad;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import servicios.TemperaturasServicio;
import vistas.TemperaturasVista;

import java.awt.Dimension;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TemperaturasControlador {

    private final TemperaturasVista vista;
    private List<TemperaturaCiudad> registros;

    public TemperaturasControlador(TemperaturasVista vista) {
        this.vista = vista;
        this.vista.setGraficarClick(evento -> graficarPromedios());
        this.vista.setConsultarExtremosClick(evento -> consultarExtremos());
        cargarDatos();
    }

    private void cargarDatos() {
        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Temperaturas.csv";
        registros = TemperaturasServicio.getDatos(nombreArchivo);
    }

    private void graficarPromedios() {
        LocalDate desde = vista.getFechaDesde();
        LocalDate hasta = vista.getFechaHasta();

        Map<String, Double> promedios = TemperaturasServicio.getPromediosPorCiudad(registros, desde, hasta);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        promedios.forEach((ciudad, promedio) -> dataset.addValue(promedio, "Temperatura Promedio", ciudad));

        JFreeChart graficador = ChartFactory.createBarChart(
                "Promedio de Temperaturas por Ciudad",
                "Ciudad",
                "Temperatura (°C)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        ChartPanel pnlGraficador = new ChartPanel(graficador);
        pnlGraficador.setPreferredSize(new Dimension(500, 270));
        vista.mostrarGrafica(pnlGraficador);
        vista.activarPestaña(0);
    }

    private void consultarExtremos() {
        LocalDate fecha = vista.getFechaConsulta();

        Optional<TemperaturaCiudad> masCalurosa = TemperaturasServicio.getMasCalurosa(registros, fecha);
        Optional<TemperaturaCiudad> menosCalurosa = TemperaturasServicio.getMenosCalurosa(registros, fecha);

        if (masCalurosa.isPresent() && menosCalurosa.isPresent()) {
            vista.mostrarResultadosExtremos(
                    masCalurosa.get().getCiudad(), masCalurosa.get().getTemperatura(),
                    menosCalurosa.get().getCiudad(), menosCalurosa.get().getTemperatura()
            );
        } else {
            vista.mostrarSinDatos();
        }
        vista.activarPestaña(1);
    }
}