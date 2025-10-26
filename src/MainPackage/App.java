/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainPackage;
import GUI.Classes.ChartClass;
import AuxClass.Cola;
import GUI.Classes.Estadisticas;
import GUI.Classes.Simulator;
import GUI.Classes.ProcessMaker;
import MainClasses.CPU;
import MainClasses.PCB;
import MainClasses.Metrics;
import MainClasses.Planificador;
import MainClasses.Proceso;
import MainClasses.ProcesoCPUBOUND;
import MainClasses.MemoryManager;
import MainClasses.Planificador;
import MainClasses.ProcesoIOBOUND;
import MainClasses.RegistrosControlEstado;
import FileFunctions.GuardadoGson;
import AuxClass.Conjunto;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author chela
 */

public class App {

    private static final App uniqueApp = new App();

    //private final SO sistemaOperativo;
    private Planificador planificador;
    private CPU cpu1;
    private CPU cpu2;
    private CPU cpu3;
    public AtomicInteger duracionCicloInstruccion = new AtomicInteger(); //Variable global que indica la duración de un ciclo de instrucción
    public boolean flagCambio = false;
    private GuardadoGson guardadoGson;
    MemoryManager memoryManager = new MemoryManager(1024, 256);
    Planificador p = new Planificador(memoryManager);


    public int relojGlobal;
    
    Metrics metrics = new Metrics();
  
    private static ChartClass chartClassSystem;
    private static ChartClass chartClassCPU1;
    private static ChartClass chartClassCPU2;
    private static ChartClass chartClassCPU3;

    private App() {
       
        this.planificador = inicializarSistemaOperativo();
    }

    public static App getInstance() {
        return uniqueApp;
    }

    private Planificador inicializarSistemaOperativo() {
        this.planificador = new Planificador(memoryManager);

        return planificador;

    }

    public void start() { 
        
    }

    public void start2() throws FontFormatException, IOException {

        /*
        CARGA DE LOS PROCESOS EN 0, ESCRITURA DE LOS PROCESOS EN 1
         */
        this.guardadoGson = new GuardadoGson(1);
        
        
        ProcessMaker processMaker = new ProcessMaker();
        processMaker.setVisible(true);

//        Simulator simulator = new Simulator("1000", 2, "");
//        simulator.setVisible(true);

        /*
        CREACION DE LAS GRAFICAS EN TIEMPO REAL
         */
        chartClassSystem = new ChartClass(0);
        chartClassCPU1 = new ChartClass(1);
        chartClassCPU2 = new ChartClass(2);
        chartClassCPU3 = new ChartClass(3);

//        Estadisticas estadistica = new Estadisticas();
//        estadistica.setVisible(true);
        //this.relojGlobal = simulator.getRelojGlobal();
    }

    public void startSimulator(int cpuQuantity) {
    // Creamos el entorno del sistema operativo
    RegistrosControlEstado environmentSO = new RegistrosControlEstado(0, 1, 0);
    PCB pcbSO = new PCB(0, "SO", "Running", environmentSO);
    ProcesoCPUBOUND pSO = new ProcesoCPUBOUND("SO", 3, "CPU BOUND", pcbSO, duracionCicloInstruccion);

    // Solo una CPU activa
    this.setCpu1(new CPU(0, null, "Activo", pSO));
    this.cpu1.setPlanificador(planificador);

    // Arrancamos solo la CPU1
    this.cpu1.start();
}


    public GuardadoGson getGuardadoGson() {
        return guardadoGson;
    }

    public void setGuardadoGson(GuardadoGson guardadoGson) {
        this.guardadoGson = guardadoGson;
    }

    public void setearProcesoACPU() {
        this.planificador.escogerProceso(getRelojGlobal());
        //this.cpu.setActualProceso(actualProceso);
    }

    public Planificador getPlanificador() {
        return planificador;
    }

    public CPU getCpu1() {
        return cpu1;
    }

    public CPU getCpu2() { //Falta getcpu3
        return cpu2;
    }

    public CPU getCpu3() {
        return cpu3;
    }

    public int getRelojGlobal() {
        return this.relojGlobal;
    }

    public static ChartClass getChartClassSystem() {
        return chartClassSystem;
    }

    public static ChartClass getChartClassCPU1() {
        return chartClassCPU1;
    }

    public static ChartClass getChartClassCPU2() {
        return chartClassCPU2;
    }

    public static ChartClass getChartClassCPU3() {
        return chartClassCPU3;
    }

    /**
     * @param cpu1 the cpu1 to set
     */
    public void setCpu1(CPU cpu1) {
        this.cpu1 = cpu1;
    }

    /**
     * @param cpu2 the cpu2 to set
     */
    public void setCpu2(CPU cpu2) {
        this.cpu2 = cpu2;
    }

    /**
     * @param cpu3 the cpu3 to set
     */
    public void setCpu3(CPU cpu3) {
        this.cpu3 = cpu3;
    }
    // Expose Metrics instance to GUI
    public Metrics getMetrics() {
        return this.metrics;
    }

}
