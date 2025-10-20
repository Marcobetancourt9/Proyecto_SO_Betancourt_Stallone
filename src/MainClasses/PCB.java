/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

import AuxClass.List;

/**
 *
 * @author chela
 */
public class PCB {
    // registros_ambiente posible lista anidada
    
    /* 
        Es posible usar un id tipo hash?
    */
    private int id; 
    private String nombre_proceso;
    /* 
        Posibles estados: Running, Blocked, Ready
        Finished??? O Exit creo que le dice el prof
    */
    private String estado;
    private RegistrosControlEstado ambienteEjecucion;

    public PCB(int id, String nombre_proceso, String estado, RegistrosControlEstado ambienteEjecucion) {
        this.id = id;
        this.nombre_proceso = nombre_proceso;
        this.estado = estado;
        this.ambienteEjecucion = ambienteEjecucion;
    }

    public RegistrosControlEstado getAmbienteEjecucion() {
        return ambienteEjecucion;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nombre_proceso
     */
    public String getNombre_proceso() {
        return nombre_proceso;
    }

    /**
     * @param nombre_proceso the nombre_proceso to set
     */
    public void setNombre_proceso(String nombre_proceso) {
        this.nombre_proceso = nombre_proceso;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @param ambienteEjecucion the ambienteEjecucion to set
     */
    public void setAmbienteEjecucion(RegistrosControlEstado ambienteEjecucion) {
        this.ambienteEjecucion = ambienteEjecucion;
    }
    
    
    
    
}
