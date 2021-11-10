package IncDec;

public class Carrera {
    // N, M, variable
    public static final int N = 100, M = 1000;
    public static MonitorIncDec monitor = new MonitorIncDec();
    
    public static void main(String args[]) {
        Thread[] hilos = new Thread[2*M];
        
        // Creamos M procesos de incremento
        for (int i = 0; i < M; ++i) {
            hilos[i] = new Thread(new Incrementador(monitor));
            hilos[i].start();
        }
        
        // Creamos M procesos de decremento
        for (int i = M; i < 2*M; ++i) {
            hilos[i] = new Thread(new Decrementador(monitor));
            hilos[i].start();
        }
        
        for (int i = 0; i < 2*M; ++i) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Muestro el resultado de la variable
        System.out.println("- Todos los hilos han terminado su ejecución");
        System.out.println("> La variable var = " + monitor.getVar());
    }
}

class Incrementador implements Runnable {
    private MonitorIncDec monitor;
    
    public Incrementador(MonitorIncDec monitor) {
        this.monitor = monitor;
    }
    
    // Incrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            monitor.incrementar();
        }
    }
}

class Decrementador implements Runnable {
    private MonitorIncDec monitor;
    
    public Decrementador(MonitorIncDec monitor) {
        this.monitor = monitor;
    }
    
    // Decrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            monitor.decrementar();
        }
    }
}
