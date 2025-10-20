/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author chela
 */
public class Metrics {
    private AtomicLong completedCount = new AtomicLong(0);
    private AtomicLong cpuBusyCycles = new AtomicLong(0);
    private AtomicLong totalCycles = new AtomicLong(0);
    private Map<Integer, Long> cpuTimePerProcess = new ConcurrentHashMap<>();
    private Map<Integer, Long> firstRunTimestamp = new ConcurrentHashMap<>();
    private Map<Integer, Long> arrivalTimestamp = new ConcurrentHashMap<>();

    public void markArrival(Proceso p, long cycle) {
        arrivalTimestamp.put((int)p.getId(), cycle);
    }
    public void markFirstRun(Proceso p, long cycle) {
        firstRunTimestamp.putIfAbsent((int)p.getId(), cycle);
    }
    public void addCpuTime(Proceso p, long cycles) {
        cpuTimePerProcess.merge((int)p.getId(), cycles, Long::sum);
        cpuBusyCycles.addAndGet(cycles);
    }
    public void markCompletion(Proceso p) {
        completedCount.incrementAndGet();
    }
    public void incrementTotalCycles() {
        totalCycles.incrementAndGet();
    }

    // getters
    public double getThroughput() {
        long t = totalCycles.get();
        return t==0 ? 0 : (double) completedCount.get() / (double) t;
    }
    public double getCpuUtilization() {
        long t = totalCycles.get();
        return t==0 ? 0 : (double) cpuBusyCycles.get() / (double) t;
    }
    public double getAverageResponseTime() {
        long sum = 0, cnt = 0;
        for (Integer id : firstRunTimestamp.keySet()) {
            Long first = firstRunTimestamp.get(id);
            Long arr = arrivalTimestamp.get(id);
            if (first != null && arr != null) { sum += (first - arr); cnt++; }
        }
        return cnt==0 ? 0 : (double) sum / cnt;
    }
    public double getJainsFairness() {
        double sum = 0;
        double sumSq = 0;
        int n = cpuTimePerProcess.size();
        for (Long v : cpuTimePerProcess.values()) {
            sum += v;
            sumSq += v*v;
        }
        if (n==0) return 0;
        return (sum*sum) / (n * sumSq);
    }
    
}

