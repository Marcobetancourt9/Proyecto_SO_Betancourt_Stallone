package MainClasses;

class HiloControlado extends Thread {
    private volatile boolean pausado = false;

    public void pausar() {
        pausado = true;
    }

    public void reanudar() {
        pausado = false;
        synchronized (this) {
            notify(); // Despertar si estaba esperando
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (pausado) {
                    try {
                        System.out.println("Hilo pausado. Esperando...");
                        wait(); // Esperar hasta ser notificado
                    } catch (InterruptedException e) {
                        System.out.println("Hilo interrumpido.");
                    }
                }
            }
            // Código principal del hilo
            System.out.println("Hilo en ejecución...");
            try {
                Thread.sleep(1000); // Simula trabajo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

