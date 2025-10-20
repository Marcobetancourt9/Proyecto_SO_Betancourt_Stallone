/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

/**
 *
 * @author chela
 */
public class SO extends Thread {
    private PCB currentProcess;
    private PCB newProcess;
    public Planificador planificador;
    public int action; //Acciones del SO

    public SO(PCB CurrentProcess, PCB NewProcess, Planificador planificador) {
        this.currentProcess = CurrentProcess;
        this.newProcess = NewProcess;
        this.planificador = planificador;
        this.action = 0;
    }
    
    private void ejecutarProceso(Planificador planificador){
        System.out.println("SO poniendo en ejecución al prox proceso...");
    }
    
    private void generarExcepcion(CPU cpu, Proceso proceso) {
        proceso.getPCB_proceso().setEstado("Blocked");
        System.out.println("Proceso " + proceso.getNombreProceso() + " bloqueado por excepción I/O");
        //poner semáforos y mover a las colas. Crear proceso que sea del SO 
    }
    
    
   
    public void satisfacerExcepcion(CPU cpu, Proceso proceso) {
        proceso.getPCB_proceso().setEstado("Ready");
        System.out.println("Proceso " + proceso.getNombreProceso() + " listo para ejecutarse nuevamente");
    }
   
   @Override
   public void run(){
       
   }
}
