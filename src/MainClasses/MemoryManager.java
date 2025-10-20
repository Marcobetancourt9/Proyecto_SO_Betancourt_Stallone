/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

/**
 *
 * @author marco
 */
public class MemoryManager {
    private int memoryAvailable;
    private final int threshold; // cuando baja de threshold, suspendemos

    public MemoryManager(int totalMemory, int threshold) {
        this.memoryAvailable = totalMemory;
        this.threshold = threshold;
    }

    public synchronized boolean requestMemory(Proceso p) {
        int needed = p.getMemoryNeeded();
        if (needed <= memoryAvailable) {
            memoryAvailable -= needed;
            return true;
        } else {
            return false;
        }
    }

    public synchronized void freeMemory(Proceso p) {
        memoryAvailable += p.getMemoryNeeded();
    }

    public synchronized int getMemoryAvailable() {
        return memoryAvailable;
    }
}
