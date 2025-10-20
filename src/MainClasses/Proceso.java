/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author chela
 */
public class Proceso extends Thread{
    private String nombre_proceso;
    private int cant_instrucciones;
    private String tipo;
    private PCB PCB_proceso;
    private int tiempoRestante; // Tiempo restante en ciclos
    private int tiempoEnCola; // Sea cola de listos o bloqueados
    private int tasaRespuesta;
    private int cicloEntradaListo; //último ciclo global en el que entró a la cola de listos
    private int sleepTime;

    private AtomicInteger ciclosDuracion;
    
    public Proceso(String nombre_proceso, int cant_instrucciones, String tipo, PCB PCB_proceso){
        this.nombre_proceso = nombre_proceso;
        this.cant_instrucciones = cant_instrucciones;
        this.PCB_proceso = PCB_proceso;
        this.tipo = tipo;
        this.tiempoRestante = cant_instrucciones; // Inicialmente son iguales
        this.tiempoEnCola = 1;
        this.tasaRespuesta = 0;
        

    }


    private int priority = 0; // menor = mayor prioridad, por ejemplo
    private int remainingQuantum = 0;
  

// Prioridad
public int getPrioridad() {
    return priority;
}

public void setPrioridad(int prioridad) {
    this.priority = prioridad;
}

// Remaining quantum
public int getRemainingQuantum() {
    return remainingQuantum;
}

public void setRemainingQuantum(int remainingQuantum) {
    this.remainingQuantum = remainingQuantum;
}

public void decrementRemainingQuantum() {
    if (this.remainingQuantum > 0) this.remainingQuantum--;
}

public void resetRemainingQuantum(int quantum) {
    this.remainingQuantum = quantum;
}

    public int getTasaRespuesta() {
        return tasaRespuesta;
    }

    public void setTasaRespuesta(int tasaRespuesta) {
        this.tasaRespuesta = tasaRespuesta;
    }
    
    
    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public int getTiempoEnCola() {
        return tiempoEnCola;
    }

    public void setTiempoEnCola(int tiempoEnCola) {
        this.tiempoEnCola = tiempoEnCola;
    }
    
    

    public void reducirTiempo(int tiempo) {
        this.setTiempoRestante(this.tiempoRestante - tiempo);
    }

    public String getNombreProceso() {
        return nombre_proceso;
    }
    
    /**
     *
     */
    @Override
    public void run(){
    }

    /**
     * @return the cant_instrucciones
     */
    public int getCant_instrucciones() {
        return cant_instrucciones;
    }

    public String getNombre_proceso() { //esto está repetido chamo
        return nombre_proceso;
    }

    public String getTipo() {
        return tipo;
    }

    public PCB getPCB_proceso() {
        return PCB_proceso;
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
    public void setCiclosDuracion(AtomicInteger ciclosDuracion) {
        this.ciclosDuracion = ciclosDuracion;
    }
    

    /**
     * @param cant_instrucciones the cant_instrucciones to set
     */
    public void setCant_instrucciones(int cant_instrucciones) {
        this.cant_instrucciones = cant_instrucciones;
    }

    /**
     * @param tiempoRestante the tiempoRestante to set
     */
    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
    
    
    //Estos solo es para q no explote la broma
    /**
     * @return the cicloGenerarExcepcion
     */
    public int getCicloGenerarExcepcion() {
        return 0;
    }

    /**
     * @param cicloGenerarExcepcion the cicloGenerarExcepcion to set
     */
    public void setCicloGenerarExcepcion(int cicloGenerarExcepcion) {
        
    }

    /**
     * @return the cicloSatisfacerExcepcion
     */
    public int getCicloSatisfacerExcepcion() {
        return 0;
    }

    /**
     * @param cicloSatisfacerExcepcion the cicloSatisfacerExcepcion to set
     */
    public void setCicloSatisfacerExcepcion(int cicloSatisfacerExcepcion) {
       
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
