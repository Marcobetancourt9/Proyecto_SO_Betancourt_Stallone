/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileFunctions;

import AuxClass.Cola;
import AuxClass.Nodo;
import MainClasses.PCB;
import MainClasses.Proceso;
import MainClasses.ProcesoCPUBOUND;
import MainClasses.ProcesoIOBOUND;
import MainPackage.App;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import MainClasses.RegistrosControlEstado;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Angelo
 */
public class GuardadoGson {

    private final App app = App.getInstance();

    public GuardadoGson(int guardarOcargar) {
        if (guardarOcargar == 0) {
            //GUARDAMOS
            GuardadoGson();
        } else {
            CargarDatosGson();
        }
    }

    public void GuardadoGson() {

        JsonObject parametrosGenerales = new JsonObject();

        parametrosGenerales.addProperty("cicloDuracion", app.duracionCicloInstruccion);
        parametrosGenerales.addProperty("nombreAlgoritmo", app.getPlanificador().getNombreAlgoritmo());

        JsonArray colaListosArray = new JsonArray();
        Cola<Proceso> colaListos = app.getPlanificador().getColaListos();
        Nodo<Proceso> currentProcess = colaListos.getHead();

        while (currentProcess != null) {
            Proceso proceso = currentProcess.gettInfo();
            PCB pcb = proceso.getPCB_proceso();

            JsonObject procesoJson = new JsonObject();
            procesoJson.addProperty("nombre", proceso.getNombreProceso());
            procesoJson.addProperty("cantidadInstrucciones", proceso.getCant_instrucciones());
            procesoJson.addProperty("tipo", proceso.getTipo());
            procesoJson.addProperty("estado", pcb.getEstado());
            procesoJson.addProperty("idPCB", pcb.getId());
            procesoJson.addProperty("ciclosDuracion", proceso.getCiclosDuracion());

            if (proceso.getTipo().equals("I/O BOUND")) {
                procesoJson.addProperty("cicloGenerarExcepcion", proceso.getCicloGenerarExcepcion());
                procesoJson.addProperty("cicloSatisfacerExcepcion", proceso.getCicloSatisfacerExcepcion());
            }

            procesoJson.addProperty("PSW", pcb.getAmbienteEjecucion().getPsw());
            procesoJson.addProperty("PC", pcb.getAmbienteEjecucion().getPc());
            procesoJson.addProperty("MAR", pcb.getAmbienteEjecucion().getMAR());

            colaListosArray.add(procesoJson);

            currentProcess = currentProcess.getpNext();
        }
        // creacion de json padre
        JsonObject root = new JsonObject();

        // union de ambos json
        root.add("parametrosGenerales", parametrosGenerales);
        root.add("colaListos", colaListosArray);

        try (FileWriter writer = new FileWriter("simulacion.json")) {
            writer.write(root.toString());
            System.out.println("Datos de la simulación guardados en simulacion.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void CargarDatosGson() {
        Gson gson = new Gson();

        try (Reader reader = new FileReader("simulacion.json")) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            // Parámetros generales, obtenemos ese json object
            JsonObject parametrosGenerales = root.getAsJsonObject("parametrosGenerales");
            int cicloDuracion = parametrosGenerales.get("cicloDuracion").getAsInt();
            String nombreAlgoritmo = parametrosGenerales.get("nombreAlgoritmo").getAsString();

            app.duracionCicloInstruccion = new AtomicInteger(cicloDuracion);
            app.getPlanificador().setNombreAlgoritmo(nombreAlgoritmo);
            System.out.println("Ciclo de duración: " + cicloDuracion);
            System.out.println("Nombre del algoritmo: " + nombreAlgoritmo);

            // Cola de listos (array plano)
            JsonArray colaListosArray = root.getAsJsonArray("colaListos");
            app.getPlanificador().getColaListos().vaciar();

            for (JsonElement elemento : colaListosArray) {
                JsonObject procesoJson = elemento.getAsJsonObject();

                String nombre = procesoJson.get("nombre").getAsString();
                int cantidadInstrucciones = procesoJson.get("cantidadInstrucciones").getAsInt();
                String estado = procesoJson.get("estado").getAsString();
                int idPcb = procesoJson.get("idPCB").getAsInt();
                int PC = procesoJson.get("PC").getAsInt();
                int MAR = procesoJson.get("MAR").getAsInt();
                String tipo = procesoJson.get("tipo").getAsString();
                AtomicInteger ciclosDuracion = new AtomicInteger(procesoJson.get("ciclosDuracion").getAsInt());

                // Creamos el PCB
                RegistrosControlEstado environment = new RegistrosControlEstado(0, PC, MAR);
                PCB pcb = new PCB(idPcb, nombre, estado, environment);

                Proceso proceso = null;

                if (tipo.equalsIgnoreCase("CPU BOUND")) {

                    proceso = new ProcesoCPUBOUND(nombre, cantidadInstrucciones, tipo, pcb, ciclosDuracion);
                } else if (tipo.equalsIgnoreCase("I/O BOUND")) {

                    int cicloGenerarExcepcion = procesoJson.get("cicloGenerarExcepcion").getAsInt();
                    int cicloSatisfacerExcepcion = procesoJson.get("cicloSatisfacerExcepcion").getAsInt();
                    proceso = new ProcesoIOBOUND(nombre, cantidadInstrucciones, tipo, pcb, ciclosDuracion,
                            cicloGenerarExcepcion, cicloSatisfacerExcepcion);
                } else {
                    // Opcional: crear un proceso genérico o manejar el error
                    System.out.println("Tipo de proceso desconocido para: " + nombre);
                }

                if (proceso != null) {
                    app.getPlanificador().getColaListos().encolar(proceso);
                    System.out.println("Proceso leído: " + nombre + ", Instrucciones: " + cantidadInstrucciones
                            + ", Estado: " + estado + ", PC: " + PC + ", MAR: " + MAR + ", Tipo: " + tipo);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
