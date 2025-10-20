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
public class ProcesoIOBOUND extends Proceso {

    private AtomicInteger ciclosDuracion;
    private int cicloGenerarExcepcion;
    private int cicloSatisfacerExcepcion;
    private int contadorCiclos;
    private int cicloEntradaListo; //último ciclo global en el que entró a la cola de listos

    private int tiempoEnCola;
    private int sleepTime; //tiempo del sleep

    private final App app = App.getInstance();

    public ProcesoIOBOUND(String nombre_proceso, int cant_instrucciones, String tipo, PCB PCB_proceso, AtomicInteger ciclosDuracion, int cicloGenerarExcepcion, int cicloSatisfacerExcepcion) {
        super(nombre_proceso, cant_instrucciones, "I/O BOUND", PCB_proceso);
        this.ciclosDuracion = ciclosDuracion;
        this.cicloGenerarExcepcion = cicloGenerarExcepcion;
        this.cicloSatisfacerExcepcion = cicloSatisfacerExcepcion;
        this.contadorCiclos = 0;
        this.cicloEntradaListo = app.getRelojGlobal();
        this.tiempoEnCola = 1;
        this.sleepTime = cicloGenerarExcepcion;
    }

    private void generarExcepcion(Planificador planificador) {
        this.getPCB_proceso().setEstado("Blocked");
        System.out.println("Proceso " + this.getNombreProceso() + " bloqueado por excepción I/O");

        //app.getPlanificador().bloquearProceso(this, this.cicloGenerarExcepcion, this.cicloSatisfacerExcepcion);
        
    }

    public int getContadorCiclos() {
        return contadorCiclos;
    }

    public void setContadorCiclos(int contadorCiclos) {
        this.contadorCiclos = contadorCiclos;
    }

    public int getTiempoEnCola() {
        return tiempoEnCola;
    }

    public void setTiempoEnCola(int tiempoEnCola) {
        this.tiempoEnCola = tiempoEnCola;
    }
    
    private void terminar(){
        this.getPCB_proceso().setEstado("Exit");

        if (this.getNombreProceso() != "SO"){
            System.out.println("TERMINO UN PROCESO IO BOUND");
            app.getPlanificador().terminarProceso(this);// Encolar el proceso en Terminados
        }

    }

    private void satisfacerExcepcion(Planificador planificador) {
        System.out.println("Proceso " + this.getNombreProceso() + " listo para ejecutarse nuevamente");
        this.getPCB_proceso().setEstado("Ready"); //En realidad se va a ready y hay que tener un semáforo para la cola de listos
        this.setCicloEntradaListo(app.getRelojGlobal()); 

    }

    @Override
    public void run() {
        this.getPCB_proceso().setEstado("Running");
        while (true) {
            if (this.getTiempoRestante() == 0) {
                this.getPCB_proceso().setEstado("Exit");
                terminar();
            }
            if ("Running".equals(this.getPCB_proceso().getEstado())) {
//                System.out.println("Proceso " + this.getNombreProceso() + " ejecutándose");
//                System.out.println("Cant_instrucciones: " + this.getCant_instrucciones());
                int MAR_num = this.getCant_instrucciones() - this.getTiempoRestante();
                this.getPCB_proceso().getAmbienteEjecucion().setMAR(MAR_num);
                this.getPCB_proceso().getAmbienteEjecucion().setPc(MAR_num + 1);
//                System.out.println("MAR: " + this.getPCB_proceso().getAmbienteEjecucion().getMAR());
//                System.out.println("PC: " + this.getPCB_proceso().getAmbienteEjecucion().getPc());
//                System.out.println("Estado: " + this.getPCB_proceso().getEstado());
//                System.out.println("");
                this.reducirTiempo(1);
                this.contadorCiclos++;
                if (this.contadorCiclos % this.getCicloGenerarExcepcion() == 0) {
                    //AQUÏ HAY QUE USAR UN SEMÁFORO PARA AGG A LA COLA DE BLOQUEADOS
                    this.generarExcepcion(app.getPlanificador());
                } else {
                    try {
                        this.sleep(this.getCiclosDuracion().get());
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProcesoIOBOUND.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }


            }else if ("Blocked".equals(this.getPCB_proceso().getEstado())){
//                for (int i = 0; i < this.getCicloSatisfacerExcepcion(); i++) {
//                        try {
//                            this.sleep(this.getCiclosDuracion().get());
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(ProcesoIOBOUND.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                this.satisfacerExcepcion();
                System.out.println("proceso bloqueadito");
                break;
            } else if ("Ready".equals(this.getPCB_proceso().getEstado())){
                
                
                // aqui iria la suma del contador de la cola de listos.
                
                
                
                System.out.println("Proceso listo");
                break;
            }else {
                System.out.println("Proceso terminado");
                terminar();
                break;
            } 

            if (this.getTiempoRestante() == 0) {
                this.getPCB_proceso().setEstado("Exit");
                System.out.println("Proceso terminado");
                terminar();
                break;
            }
        }
    }

    /**
     * @return the ciclosDuracion
     */
    @Override
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
     * @return the cicloGenerarExcepcion
     */
    @Override
    public int getCicloGenerarExcepcion() {
        return cicloGenerarExcepcion;
    }

    /**
     * @param cicloGenerarExcepcion the cicloGenerarExcepcion to set
     */
    @Override
    public void setCicloGenerarExcepcion(int cicloGenerarExcepcion) {
        this.cicloGenerarExcepcion = cicloGenerarExcepcion;
    }

    /**
     * @return the cicloSatisfacerExcepcion
     */
    @Override
    public int getCicloSatisfacerExcepcion() {
        return cicloSatisfacerExcepcion;
    }

    /**
     * @param cicloSatisfacerExcepcion the cicloSatisfacerExcepcion to set
     */
    @Override
    public void setCicloSatisfacerExcepcion(int cicloSatisfacerExcepcion) {
        this.cicloSatisfacerExcepcion = cicloSatisfacerExcepcion;
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
