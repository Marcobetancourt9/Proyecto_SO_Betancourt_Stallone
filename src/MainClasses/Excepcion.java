/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chela
 */
public class Excepcion extends Thread {

    public Proceso proceso;
    int[] ciclos;
    public CPU cpu;
    public Planificador planificador;

    public Excepcion(Proceso proceso, CPU cpu, Planificador planificador, int[] ciclos) {
        this.proceso = proceso;
        this.cpu = cpu;
        this.planificador = planificador;
        this.ciclos = ciclos;
    }

    @Override
    public void run() {
        planificador.bloquearProceso(proceso, ciclos[0], ciclos[1]);
        try {
            proceso.sleep(proceso.getCiclosDuracion().get() * ciclos[1]);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcesoIOBOUND.class.getName()).log(Level.SEVERE, null, ex);
        }

        //ProcesoIOBOUND procesocopia = new ProcesoIOBOUND(proceso.getNombreProceso(), proceso.getCant_instrucciones(), "I/O BOUND", proceso.getPCB_proceso(), proceso.getCiclosDuracion(), ciclos[0], ciclos[1]);
        planificador.desbloquearProceso(proceso, cpu.getActualProceso(), ciclos[0], ciclos[1]);

    }
}
