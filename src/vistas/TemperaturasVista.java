package vistas;

import datechooser.beans.DateChooserCombo;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;

public class TemperaturasVista extends JFrame {

    private DateChooserCombo dccDesde, dccHasta, dccFechaConsulta;
    private JTabbedPane tpOpciones;
    private JPanel pnlGrafica;
    private JPanel pnlResultados;
    private JLabel lblResultadoExtremos;

    private JButton btnGraficar, btnConsultarExtremos;

    public TemperaturasVista() {
        setTitle("Análisis de Temperaturas");
        setSize(750, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        btnGraficar = new JButton("Graficar Promedios");
        tb.add(btnGraficar);

        btnConsultarExtremos = new JButton("Consultar Extremos");
        tb.add(btnConsultarExtremos);

        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setLayout(new FlowLayout(FlowLayout.LEFT));

        pnlDatosProceso.add(new JLabel("Desde:"));
        dccDesde = new DateChooserCombo();
        pnlDatosProceso.add(dccDesde);

        pnlDatosProceso.add(new JLabel("Hasta:"));
        dccHasta = new DateChooserCombo();
        pnlDatosProceso.add(dccHasta);

        pnlDatosProceso.add(new JLabel(" | Fecha a consultar:"));
        dccFechaConsulta = new DateChooserCombo();
        pnlDatosProceso.add(dccFechaConsulta);

        pnlGrafica = new JPanel();
        pnlResultados = new JPanel();
        lblResultadoExtremos = new JLabel("Seleccione una fecha y presione 'Consultar Extremos'");
        pnlResultados.add(lblResultadoExtremos);

        tpOpciones = new JTabbedPane();
        tpOpciones.addTab("Gráfica de Barras", new JScrollPane(pnlGrafica));
        tpOpciones.addTab("Temperaturas Extremas", pnlResultados);

        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.add(pnlDatosProceso);
        pnlPrincipal.add(tpOpciones);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlPrincipal, BorderLayout.CENTER);
    }

    public LocalDate getFechaDesde() {
        return dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getFechaHasta() {
        return dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getFechaConsulta() {
        return dccFechaConsulta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setGraficarClick(ActionListener evento) {
        btnGraficar.addActionListener(evento);
    }

    public void setConsultarExtremosClick(ActionListener evento) {
        btnConsultarExtremos.addActionListener(evento);
    }

    public void activarPestaña(int posicion) {
        if (posicion >= 0 && posicion < tpOpciones.getTabCount()) {
            tpOpciones.setSelectedIndex(posicion);
        }
    }

    public void mostrarGrafica(ChartPanel pnlGraficador) {
        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(pnlGraficador, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    public void mostrarResultadosExtremos(String ciudadMas, double tempMas, String ciudadMenos, double tempMenos) {
        lblResultadoExtremos.setText(String.format(
            "<html><h3>Resultados:</h3>" +
            "<b>Más calurosa:</b> %s (%.1f °C)<br>" +
            "<b>Menos calurosa:</b> %s (%.1f °C)</html>",
            ciudadMas, tempMas, ciudadMenos, tempMenos
        ));
    }

    public void mostrarSinDatos() {
        lblResultadoExtremos.setText("No hay datos para la fecha seleccionada.");
    }
}