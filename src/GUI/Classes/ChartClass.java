package GUI.Classes;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import AuxClass.Cola;
import AuxClass.Conjunto;
import AuxClass.Nodo;
import MainClasses.Proceso;
import MainClasses.Proceso;
import MainPackage.App;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author marco
 */

public class ChartClass {

    private final App app = App.getInstance();
    private XYSeries seriesCpuBound;
    private XYSeries seriesIoBound;
    private XYSeriesCollection dataset;
    private JFreeChart xyLineChart;
    private Timer updateTimer;

    private int id;

    public ChartClass(int id) {
        // this.id = id;
        initializeSeries();
        initializeChart();
        startDataUpdateTimer(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private void initializeSeries() {
        seriesCpuBound = new XYSeries("CPU-Bound");
        seriesIoBound = new XYSeries("I/O-Bound");
        dataset = new XYSeriesCollection();
        dataset.addSeries(seriesCpuBound);
        dataset.addSeries(seriesIoBound);
    }

    private void initializeChart() {
        xyLineChart = ChartFactory.createXYLineChart(
                "Tiempo vs Tipo de Procesos", // Título del gráfico
                "Tiempo", // Etiqueta eje X
                "Cantidad de Procesos", // Etiqueta eje Y
                dataset, // Dataset
                PlotOrientation.VERTICAL,
                true, // Mostrar leyenda
                true, // Generar tooltips
                false // URLs
        );

        customizeChart();
    }

    /**
     * Personaliza la apariencia del gráfico.
     */
    private void customizeChart() {
        XYPlot plot = xyLineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Configurar colores y estilos
        renderer.setSeriesPaint(0, Color.RED); // CPU-Bound en rojo
        renderer.setSeriesPaint(1, Color.BLUE); // I/O-Bound en azul
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
    }

    /**
     * Inicia un temporizador que actualiza las series de datos periódicamente.
     */
    private void startDataUpdateTimer(int id) {
        int delay = 1000; // Actualización cada segundo
        updateTimer = new Timer(delay, e -> updateChartData(id));
        updateTimer.start();
    }

    private void updateChartData(int id) {
        try {
            switch (id) {
                case 0:
                    updateChartDataSystem();
                    break;
                case 1:
                    updateChartDataCPU1();
                    break;
                case 2:
                    updateChartDataCPU2();
                    break;
                case 3:
                    updateChartDataCPU3();
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (Exception e) {
        }

    }

    public void updateChartDataSystem() {

        int tiempoActual = seriesCpuBound.getItemCount() + 1;

        Cola<Proceso> colaSinFiltrar = app.getPlanificador().getColaTerminados();
        Nodo<Proceso> proceso = colaSinFiltrar.getHead();

        int contadorCPUBOUND = 0;
        int contadorIOBOUND = 0;

        while (proceso != null) {
            if (proceso.gettInfo().getTipo().equals("CPU BOUND")) {
                contadorCPUBOUND++;
            } else {
                contadorIOBOUND++;
            }
            proceso = proceso.getpNext();
        }

        seriesCpuBound.addOrUpdate(tiempoActual, contadorCPUBOUND);
        seriesIoBound.addOrUpdate(tiempoActual, contadorIOBOUND);
    }

    public void updateChartDataCPU1() {
        int contadorCPUBOUND = 0;
        int contadorIOBOUND = 0;
        int tiempoActual = seriesCpuBound.getItemCount() + 1;
        try {
            Conjunto<Proceso> conjuntoCPU = app.getCpu1().getConjuntoProcesos();
            for (int i = 0; i < conjuntoCPU.getLista().size(); i++) {
                if (conjuntoCPU.getLista().get(i).getTipo().equals("CPU BOUND")) {
                    contadorCPUBOUND++;
                } else {
                    contadorIOBOUND++;
                }
            }

            seriesCpuBound.addOrUpdate(tiempoActual, contadorCPUBOUND);
            seriesIoBound.addOrUpdate(tiempoActual, contadorIOBOUND);
        } catch (Exception e) {
        }

    }

    public void updateChartDataCPU2() {

        int contadorCPUBOUND = 0;
        int contadorIOBOUND = 0;
        int tiempoActual = seriesCpuBound.getItemCount() + 1;
        try {
            Conjunto<Proceso> conjuntoCPU = app.getCpu2().getConjuntoProcesos();
            for (int i = 0; i < conjuntoCPU.getLista().size(); i++) {
                if (conjuntoCPU.getLista().get(i).getTipo().equals("CPU BOUND")) {
                    contadorCPUBOUND++;
                } else {
                    contadorIOBOUND++;
                }
            }

            seriesCpuBound.addOrUpdate(tiempoActual, contadorCPUBOUND);
            seriesIoBound.addOrUpdate(tiempoActual, contadorIOBOUND);
        } catch (Exception e) {
        }

    }

    public void updateChartDataCPU3() {

        int contadorCPUBOUND = 0;
        int contadorIOBOUND = 0;
        int tiempoActual = seriesCpuBound.getItemCount() + 1;
        try {
            Conjunto<Proceso> conjuntoCPU = app.getCpu3().getConjuntoProcesos();
            for (int i = 0; i < conjuntoCPU.getLista().size(); i++) {
                if (conjuntoCPU.getLista().get(i).getTipo().equals("CPU BOUND")) {
                    contadorCPUBOUND++;
                } else {
                    contadorIOBOUND++;
                }
            }

            seriesCpuBound.addOrUpdate(tiempoActual, contadorCPUBOUND);
            seriesIoBound.addOrUpdate(tiempoActual, contadorIOBOUND);
        } catch (Exception e) {
        }

    }

    public void clearChartData() {
        seriesCpuBound.clear();
        seriesIoBound.clear();
    }

    /**
     * Devuelve el panel del gráfico para ser agregado a la interfaz gráfica.
     *
     * @return
     */
    public ChartPanel getChartClass() {
        return new ChartPanel(xyLineChart);
    }

    /**
     * Detiene la actualización automática del gráfico.
     */
    public void stopUpdateTimer() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }
}
