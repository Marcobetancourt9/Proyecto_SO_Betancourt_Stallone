/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

import MainPackage.App;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chela
 */
public class ProcesoCPUBOUND extends Proceso {

    private AtomicInteger ciclosDuracion;
    private final App app = App.getInstance();
    private int cicloEntradaListo; //último ciclo global en el que entró a la cola de listos
    private int tiempoEnCola;
    private int sleepTime; //tiempo del sleep

    @Override
    public int getTiempoEnCola() {
        return tiempoEnCola;
    }

    @Override
    public void setTiempoEnCola(int tiempoEnCola) {
        this.tiempoEnCola = tiempoEnCola;
    }

    public ProcesoCPUBOUND(String nombre_proceso, int cant_instrucciones, String tipo, PCB PCB_proceso, AtomicInteger ciclosDuracion) {
        super(nombre_proceso, cant_instrucciones, "CPU BOUND", PCB_proceso);
        this.ciclosDuracion = ciclosDuracion;
        this.cicloEntradaListo = app.getRelojGlobal();
        this.tiempoEnCola = 1;
        this.sleepTime = cant_instrucciones;
    }

    private void terminar() {
        System.out.println("TERMINANDO PROCESO NOMBRE PROCESO -->>>" + this.getNombreProceso());

        this.getPCB_proceso().setEstado("Exit");

        if (this.getNombreProceso() != "SO") {
            app.getPlanificador().terminarProceso(this);// Encolar el proceso en Terminados
        }

    }

    @Override
    public void run() {
        while (true) {
            this.getPCB_proceso().setEstado("Running");
            if (this.getTiempoRestante() == 0) {
                this.getPCB_proceso().setEstado("Exit");
                terminar();
            }

            if ("Running".equals(this.getPCB_proceso().getEstado())) {
                // Actualizar MAR y PC
                int MAR_num = this.getCant_instrucciones() - this.getTiempoRestante();
                this.getPCB_proceso().getAmbienteEjecucion().setMAR(MAR_num);
                this.getPCB_proceso().getAmbienteEjecucion().setPc(MAR_num + 1);

                // Mostrar MAR y PC
                this.reducirTiempo(1);

                if (this.getTiempoRestante() == 0) {
                    this.getPCB_proceso().setEstado("Exit");
                    terminar();
                    break;
                    //llamar al planificador o importar App
                }
                try {
                    // Simular duración del ciclo
                    this.sleep(this.ciclosDuracion.get());

                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcesoCPUBOUND.class.getName()).log(Level.SEVERE, null, ex);
                }
                // ANTES CONSIDERAMOS "Blocked".equals(this.getPCB_proceso().getEstado()) || 
                // ANTES CONSIDERAMOS "Blocked".equals(this.getPCB_proceso().getEstado()) || 
            } else if ("Ready".equals(this.getPCB_proceso().getEstado())) {

                this.setTiempoEnCola(this.getTiempoEnCola() + 1);
                break;
            } else {
                // Añadir cpu a proceso
//                System.out.println("Proceso terminado");
                terminar();
                break;
            }
        }
    }

    public ProcesoCPUBOUND copiar() {
        return new ProcesoCPUBOUND(
                this.getNombreProceso(),
                this.getCant_instrucciones(),
                "CPU BOUND", // Tipo fijo
                this.getPCB_proceso(), // Asumiendo que PCB también tiene un método de copia
                this.ciclosDuracion // Copia profunda de ciclosDuracion
        );
    }

    /**
     * @return the ciclosDuracion
     */
    public AtomicInteger getCiclosDuracion() {
        return ciclosDuracion;
    }

    /**
     * @param ciclosDuracion the ciclosDuracion to set
     */
    @Override
    public void setCiclosDuracion(AtomicInteger ciclosDuracion) {
        this.ciclosDuracion = ciclosDuracion;
    }

    /**
     * @return the cicloEntradaListo
     */
    public int getCicloEntradaListo() {
        return cicloEntradaListo;
    }

    /**
     * @param cicloEntradaListo the cicloEntradaListo to set
     */
    public void setCicloEntradaListo(int cicloEntradaListo) {
        this.cicloEntradaListo = cicloEntradaListo;
    }

    /**
     * @return the sleepTime
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * @param sleepTime the sleepTime to set
     */
    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
