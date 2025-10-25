/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

import AuxClass.Cola;
import AuxClass.Nodo;
import MainClasses.CPU;
import MainClasses.Proceso;
import MainClasses.MemoryManager;
import MainPackage.App;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author chela
 */

public class Planificador {

    private String nombreAlgoritmo;
    private CPU cpuDefault;
    private Cola<Proceso> ColaListos;
    private Cola<Proceso> ColaBloqueados;
    private Cola<Proceso> ColaTerminados;
    private Cola<Proceso> ColaSuspendidos = new Cola<>();
    private final App app = App.getInstance();

    //private CPU cpuDefault;
    private Proceso procesoEntrante;

    private final int quantum = 5; //Quantum de tiempo de RR en ciclos
    private Semaphore semaphore; // Semáforo para garantizar exclusión mutua
    private Semaphore semaphore2; //Semáforo para exclusión mutua de la cola de bloqueados 
    private Semaphore semaphore3; //este es para la cola de terminados
    private MemoryManager memoryManager;
    
public Planificador(MemoryManager memoryManager) {
    this.memoryManager = memoryManager;
    this.ColaListos = new Cola<>();
    this.ColaBloqueados = new Cola<>();
    this.ColaTerminados = new Cola<>();
    this.ColaSuspendidos = new Cola<>();
    this.semaphore = new Semaphore(1);
    this.semaphore2 = new Semaphore(1);
    this.semaphore3 = new Semaphore(1);
    
}



    public Planificador(String nombreAlgoritmo, Cola<Proceso> ColaListos, Cola<Proceso> ColaBloqueados, Cola<Proceso> ColaTerminados, CPU cpuDefault) {
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.ColaListos = ColaListos;
        this.ColaBloqueados = ColaBloqueados;
        this.ColaTerminados = ColaTerminados;
        this.cpuDefault = cpuDefault;

        //this.cpuDefault = cpuDefault;
        this.semaphore = new Semaphore(1); // Inicializar el semáforo con un permiso disponible
        this.semaphore2 = new Semaphore(1);
        this.semaphore3 = new Semaphore(1);
    }

    public Cola<Proceso> getColaBloqueados() {
        return ColaBloqueados;
    }

    public Proceso escogerProceso(int relojGlobal) {
        System.out.println(getColaListos().travel());
        Proceso proceso = null;
        System.out.println("Escogiendo");
        try {
            semaphore.acquire(); // Adquirir el permiso del semáforo (wait)
            switch (nombreAlgoritmo) {
                case "FCFS":
                    proceso = fcfs();
                    break;
                case "RR":
                    proceso = roundRobin();
                    break;
                case "SPN":
                    proceso = spn();
                    break;
                case "SRT":
                    proceso = srt();
                    break;
                case "HRRN":
                    System.out.println("Ejecutando HRRN");
                    proceso = hrrn(relojGlobal);
                case "PRIORITY":
                    proceso = priority();
                    break;

                // Agregar otro caso para el algoritmo que falta
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(); // Liberar el permiso del semáforo (signal)
        }
        return proceso;
    }
    
    public void agregarProcesoNuevo(Proceso proceso) {
    if (memoryManager == null) {
        // En caso de que no se haya inyectado un MemoryManager aún
        getColaListos().encolar(proceso);
        return;
    }

    if (!memoryManager.requestMemory(proceso)) {
        ColaSuspendidos.encolar(proceso);
        System.out.println("Proceso " + proceso.getNombreProceso() + " suspendido por falta de memoria.");
    } else {
        getColaListos().encolar(proceso);
        System.out.println("Proceso " + proceso.getNombreProceso() + " agregado a la cola de listos.");
    }
}


    private Proceso hrrn(int relojGlobal) {
        System.out.println("Ejecutando política HRRN");

        if (getColaListos().isEmpty()) {
            return null; // Si no hay procesos listos, retorna null
        }

        // Calcular la tasa de respuesta para cada proceso en la cola
        Nodo<Proceso> actual = getColaListos().getHead();

        while (actual != null) {
            calculoRadioRespuesta(actual.gettInfo(), relojGlobal);
            actual = actual.getpNext();
        }

        ordenarColaPorRadioRespuesta(getColaListos());

        Proceso proceso = getColaListos().getHead().gettInfo();
        getColaListos().desencolar();

        return proceso;
    }

    public void calculoRadioRespuesta(Proceso proceso, int relojGlobal) {
        int aux = proceso.getTiempoRestante();
        if (aux == 0) {
            aux = 1;
        }
        proceso.setTiempoEnCola(proceso.getTiempoEnCola() + (relojGlobal - proceso.getCicloEntradaListo())); //tiempo en cola actualizado al momento de comparar
        int tasaRespuesta = (proceso.getTiempoEnCola() + aux) / aux;
        proceso.setTasaRespuesta(tasaRespuesta);
    }

    public void ordenarColaPorRadioRespuesta(Cola<Proceso> cola) {
        if (cola.isEmpty() || cola.getHead().getpNext() == null) {
            return; // La cola está vacía o tiene un solo elemento
        }

        boolean intercambiado;
        do {
            Nodo<Proceso> actual = cola.getHead();
            Nodo<Proceso> siguiente = actual.getpNext();
            intercambiado = false;

            while (siguiente != null) {
                // Comparar la tasa de respuesta entre los nodos
                if (actual.gettInfo().getTasaRespuesta() < siguiente.gettInfo().getTasaRespuesta()) {
                    // Intercambiar los datos de los nodos
                    Proceso temp = actual.gettInfo();
                    actual.settInfo(siguiente.gettInfo());
                    siguiente.settInfo(temp);
                    intercambiado = true;
                }
                // Avanzar a los siguientes nodos
                actual = siguiente;
                siguiente = siguiente.getpNext();
            }
        } while (intercambiado);
    }

    private Proceso fcfs() {
        System.out.println("Escogiendo en fcfs");

        // Verifica si la cola de procesos está vacía
        if (getColaListos().isEmpty()) {
            return null; // Si no hay procesos listos, retorna null
        }

        Proceso proceso = getColaListos().getHead().gettInfo(); // Obtener el primer proceso
        getColaListos().desencolar(); // Eliminar de la cola
        System.out.println(proceso + " escogidooo");
        return proceso; // Retornar el proceso
    }

    
    
    /*La diferencia es que este se ejecuta 
    cada vez que termina el quantum de tiempo*/
    private Proceso roundRobin() {
        if (getColaListos().isEmpty()) {
            return null; // Si no hay procesos listos, retorna null
        }

        Proceso proceso = getColaListos().getHead().gettInfo(); // Obtener el primer proceso 
        getColaListos().desencolar(); // Eliminar de la cola
        return proceso; // Retornar el proceso
    }

    private Proceso spn() {
        if (getColaListos().isEmpty()) {
            return null; // Si no hay procesos listos, retorna null
        }
        System.out.println("Antes de ordenar:");
        System.out.println(getColaListos().travel());

        // Ordenar la cola por número de instrucciones antes de buscar el proceso más corto
        ordenarColaPorNumeroInstrucciones(getColaListos()); //Quizá haya que cambiar por el método que ordena por tiempo restante
        System.out.println("Después de ordenar:");
        System.out.println(getColaListos().travel());
        // Obtener el proceso con el menor número de instrucciones
        Proceso procesoMasCorto = getColaListos().getHead().gettInfo();
        getColaListos().desencolar(); // Eliminar de la cola

        return procesoMasCorto; // Retornar el proceso que se ha ejecutado
    }

    public Proceso priority() {
    if (ColaListos.isEmpty()) return null;
    Nodo<Proceso> current = ColaListos.getHead();
    Proceso mejor = null;
    while (current != null) {
        Proceso p = current.gettInfo();
        if (mejor == null || p.getPriority() < mejor.getPriority()) {
            mejor = p;
        }
        current = current.getpNext();
    }
    if (mejor != null) {
        getColaListos().desencolar();  // usa el método eliminar(id) que ya tienes
    }

    return mejor;
}
    
    private Proceso srt() {
        if (getColaListos().isEmpty()) {
            return null;
        }
        ordenarColaPorTiempoRestante(getColaListos());
        Proceso shorterProcess = getColaListos().getHead().gettInfo();
        getColaListos().desencolar(); // Eliminar de la cola
        //Proceso cpuCurrentProcess = cpuDefault.getActualProceso();
        return shorterProcess;
//        if (shorterProcess.getCant_instrucciones() < cpuCurrentProcess.getCant_instrucciones()) {
//            cpuDefault.setActualProceso(shorterProcess);
//        }
    }

    public void ordenarColaPorNumeroInstrucciones(Cola<Proceso> cola) {

        if (cola.isEmpty() || cola.getHead().getpNext() == null) {
            return; // La cola está vacía o tiene un solo elemento
        }

        boolean intercambiado;
        do {
            Nodo<Proceso> actual = cola.getHead();
            Nodo<Proceso> siguiente = actual.getpNext();
            intercambiado = false;

            while (siguiente != null) {
                // Comparar el número de instrucciones entre los nodos
                if (actual.gettInfo().getCant_instrucciones() > siguiente.gettInfo().getCant_instrucciones()) {
                    // Intercambiar los datos de los nodos
                    Proceso temp = actual.gettInfo();
                    actual.settInfo(siguiente.gettInfo());
                    siguiente.settInfo(temp);
                    intercambiado = true;
                }
                // Avanzar a los siguientes nodos
                actual = siguiente;
                siguiente = siguiente.getpNext();
            }
        } while (intercambiado);
    }

    // Nuevo método para ordenar por tiempo restante
    public void ordenarColaPorTiempoRestante(Cola<Proceso> cola) {
        if (cola.isEmpty() || cola.getHead().getpNext() == null) {
            return; // La cola está vacía o tiene un solo elemento
        }

        boolean intercambiado;
        do {
            Nodo<Proceso> actual = cola.getHead();
            Nodo<Proceso> siguiente = actual.getpNext();
            intercambiado = false;

            while (siguiente != null) {
                // Comparar el tiempo restante entre los nodos
                if (actual.gettInfo().getTiempoRestante() > siguiente.gettInfo().getTiempoRestante()) {
                    // Intercambiar los datos de los nodos
                    Proceso temp = actual.gettInfo();
                    actual.settInfo(siguiente.gettInfo());
                    siguiente.settInfo(temp);
                    intercambiado = true;
                }
                // Avanzar a los siguientes nodos
                actual = siguiente;
                siguiente = siguiente.getpNext();
            }
        } while (intercambiado);
    }

    public void expulsarProceso(Proceso proceso) {
        try {
            semaphore.acquire(); // Adquirir el permiso del semáforo (wait)
            proceso.getPCB_proceso().setEstado("Ready"); // Cambiar el estado a Ready
            int tiempoRestante = proceso.getTiempoRestante();
            //Quería usar el metodo copiar pero no me deja

            if (proceso.getTipo() == "CPU BOUND") {
                ProcesoCPUBOUND proceso2 = new ProcesoCPUBOUND(proceso.getNombreProceso(), proceso.getCant_instrucciones(), "CPU BOUND", proceso.getPCB_proceso(), proceso.getCiclosDuracion());
                proceso2.setTiempoRestante(tiempoRestante);
                proceso2.getPCB_proceso().setEstado("Ready");
                if (proceso.getNombreProceso() != "SO") {
                    this.getColaListos().encolar(proceso2); // Encolar el proceso en Listos
                }
            } else {
                ProcesoIOBOUND proceso2 = new ProcesoIOBOUND(proceso.getNombreProceso(), proceso.getCant_instrucciones(), "I/O BOUND", proceso.getPCB_proceso(), proceso.getCiclosDuracion(), proceso.getCicloGenerarExcepcion(), proceso.getCicloSatisfacerExcepcion());
                System.out.println("Al hilo le fantan " + tiempoRestante + " instrucciones");
                System.out.println(proceso2.getTiempoRestante());
                proceso2.setTiempoRestante(tiempoRestante);
                proceso2.getPCB_proceso().setEstado("Ready");
                if (proceso.getNombreProceso() != "SO") {
                    this.getColaListos().encolar(proceso2); // Encolar el proceso en Listos
                }
            }

            System.out.println("Proceso: " + proceso.getNombreProceso() + "expulsado de CPU");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(); // Liberar el permiso del semáforo (signal)
        }
    }

    public void bloquearProceso(Proceso proceso, int CiclosGenerar, int CicloSatisfacer) { //realmente lo que hace es meterlo a la cola de bloqueados
        try {
            semaphore2.acquire(); // Adquirir el permiso del semáforo (wait)
            proceso.getPCB_proceso().setEstado("Blocked"); // Cambiar el estado a Blocked
            int tiempoRestante = proceso.getTiempoRestante();
            //Quería usar el metodo copiar pero no me deja
            ProcesoIOBOUND proceso2 = new ProcesoIOBOUND(proceso.getNombreProceso(), proceso.getCant_instrucciones(), "I/O BOUND", proceso.getPCB_proceso(), proceso.getCiclosDuracion(), CiclosGenerar, CicloSatisfacer);
            System.out.println("Al hilo le fantan " + tiempoRestante + " instrucciones");
            System.out.println(proceso2.getTiempoRestante());

            this.getColaBloqueados().encolar(proceso);

            /*
            
            TE COMENTE LO DE ENCOLAR EL PROCESO COPIA
            
             */
//            proceso2.setTiempoRestante(tiempoRestante);
//            proceso2.getPCB_proceso().setEstado("Blocked");
//
//            this.getColaBloqueados().encolar(proceso2); // Encolar el proceso en bloqueados
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore2.release(); // Liberar el permiso del semáforo (signal)
        }
    }

    public void desbloquearProceso(Proceso proceso, Proceso procesoEnEjecucion, int CiclosGenerar, int CicloSatisfacer) { //necesitas es proceso que ya esperó por la E/S y el proceso actual en ejecución para sacarlo del cpu
        //Sacar al proceso de la cola de bloqueados
        try {
            semaphore2.acquire(); // Adquirir el permiso del semáforo (wait)
            //proceso.getPCB_proceso().setEstado("Blocked"); // Cambiar el estado a Ready
            System.out.println("sacando al proceso de la cola de bloqueados");
            this.getColaBloqueados().desencolarEspecifico(proceso); // Desencolar el proceso

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore2.release(); // Liberar el permiso del semáforo (signal)
        }

        //Expulsar proceso en ejecución y colocar ambos en la cola de listos
        try {
            semaphore.acquire(); // Adquirir el permiso del semáforo (wait)
            procesoEnEjecucion.getPCB_proceso().setEstado("Ready"); // Cambiar el estado a Ready
            int tiempoRestante = procesoEnEjecucion.getTiempoRestante();
            //Quería usar el metodo copiar pero no me deja

            if (procesoEnEjecucion.getTipo() == "CPU BOUND") {
                ProcesoCPUBOUND proceso2 = new ProcesoCPUBOUND(proceso.getNombreProceso(), proceso.getCant_instrucciones(), "CPU BOUND", proceso.getPCB_proceso(), proceso.getCiclosDuracion());
                System.out.println("Al hilo le fantan " + tiempoRestante + " instrucciones");
                System.out.println(proceso2.getTiempoRestante());
                proceso2.setTiempoRestante(tiempoRestante);
                proceso2.getPCB_proceso().setEstado("Ready");
                if (procesoEnEjecucion.getNombreProceso() != "SO") {
                    this.getColaListos().encolar(proceso2); // Encolar el proceso en Listos
                }
            } else {
                ProcesoIOBOUND proceso2 = new ProcesoIOBOUND(procesoEnEjecucion.getNombreProceso(), procesoEnEjecucion.getCant_instrucciones(), "I/O BOUND", procesoEnEjecucion.getPCB_proceso(), procesoEnEjecucion.getCiclosDuracion(), procesoEnEjecucion.getCicloGenerarExcepcion(), procesoEnEjecucion.getCicloSatisfacerExcepcion());
                System.out.println("Al hilo le fantan " + tiempoRestante + " instrucciones");
                System.out.println(proceso2.getTiempoRestante());
                proceso2.setTiempoRestante(tiempoRestante);
                proceso2.getPCB_proceso().setEstado("Ready");
                if (proceso.getNombreProceso() != "SO") {
                    this.getColaListos().encolar(proceso2); // Encolar el proceso en Listos
                }
            }

            int tiempoRestante2 = proceso.getTiempoRestante();
            ProcesoIOBOUND procesocopia = new ProcesoIOBOUND(proceso.getNombreProceso(), proceso.getCant_instrucciones(), "I/O BOUND", proceso.getPCB_proceso(), proceso.getCiclosDuracion(), CiclosGenerar, CicloSatisfacer);
            procesocopia.setTiempoRestante(tiempoRestante2);
            this.getColaListos().encolar(procesocopia);
            System.out.println("Encolados ya");
            System.out.println("El proceso en la cola de litos es.." + ColaListos.getTail().gettInfo().getNombreProceso());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(); // Liberar el permiso del semáforo (signal)
        }

    }

    public Proceso getShorterProcess() {
        Proceso p = null;
//        try {
//            semaphore.acquire();
        ordenarColaPorTiempoRestante(ColaListos);
        if (ColaListos.getHead() != null) {
            p = ColaListos.getHead().gettInfo();
        }

//        } catch (InterruptedException ex) {
//            Logger.getLogger(Planificador.class.getName()).log(Level.SEVERE, null, ex);
//        }finally {
//            semaphore.release();
//        }
        return p;
    }

    public Proceso getHighestRatioProcess() {
        Proceso p = null;
//        try {
//            semaphore.acquire();
        ordenarColaPorRadioRespuesta(ColaListos);
        if (ColaListos.getHead() != null) {
            p = ColaListos.getHead().gettInfo();
        }

//        } catch (InterruptedException ex) {
//            Logger.getLogger(Planificador.class.getName()).log(Level.SEVERE, null, ex);
//        }finally {
//            semaphore.release();
//        }
        return p;
    }

public void terminarProceso(Proceso procesoTerminado) {
    try {
        semaphore3.acquire(); //wait
        procesoTerminado.getPCB_proceso().setEstado("Exit");
        if (!procesoTerminado.getNombreProceso().equals("SO")) {
            this.ColaTerminados.encolar(procesoTerminado);
            System.out.println("SOMOS TERMINADOS EN COLA " + this.ColaTerminados.getSize());
        }
    } catch (InterruptedException ex) {
        Logger.getLogger(Planificador.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        semaphore3.release();
    }

    // Libera memoria del proceso terminado

    // Intentar reanudar procesos suspendidos
while (!ColaSuspendidos.isEmpty()) {
    Nodo<Proceso> nodo = ColaSuspendidos.getHead();
    if (nodo == null) break; // por seguridad
    Proceso p = nodo.gettInfo();
    if (memoryManager.requestMemory(p)) {
        ColaSuspendidos.desencolar();
        ColaListos.encolar(p);
        System.out.println("Proceso " + p.getNombreProceso() + " reanudado desde suspendido.");
    } else {
        break; // no hay memoria suficiente
    }
}
}


    public void satisfacerExcepcion(Proceso proceso) {
        // Reactivar el proceso bloqueado
        //ColaBloqueados.eliminar(proceso);
        getColaListos().encolar(proceso);
    }

    public String getNombreAlgoritmo() {
        return nombreAlgoritmo;
    }

    public void setNombreAlgoritmo(String nombreAlgoritmo) {
        this.nombreAlgoritmo = nombreAlgoritmo;
    }

    /**
     * @return the quantum
     */
    public int getQuantum() {
        return quantum;
    }

    /**
     * @return the ColaListos
     */
    public Cola<Proceso> getColaListos() {
        return ColaListos;
    }

    /**
     * @return the ColaTerminados
     */
    public Cola<Proceso> getColaTerminados() {
        return ColaTerminados;
    }

    public Cola<Proceso> getColaSuspendidos() {
        return ColaSuspendidos;
                }

}
