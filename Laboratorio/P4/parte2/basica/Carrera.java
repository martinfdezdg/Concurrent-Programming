package basica;

public class Carrera {
    // Numero de hilos productores y consumidores
    public static final int N_PROD = 100;
    public static final int N_CONS = 100;
    // Creacion del almacen
    private static MonitorMultiBuffer monitor = new MonitorMultiBuffer();
    
    public static void main(String args[]) {
        Thread[] productores = new Thread[N_PROD];
        Thread[] consumidores = new Thread[N_CONS];

        // Creamos N_PROD procesos productores
        for (int i = 0; i < N_PROD; ++i) {
            productores[i] = new Thread(new Productor(i+1,monitor));
            productores[i].start();
        }
        
        // Creamos N_CONS procesos consumidores
        for (int i = 0; i < N_CONS; ++i) {
            consumidores[i] = new Thread(new Consumidor(i+1,monitor));
            consumidores[i].start();
        }
    }
}

class Productor implements Runnable {
    private int pid;
    private MonitorMultiBuffer monitor;
    
    public Productor(int pid, MonitorMultiBuffer monitor) {
        this.pid = pid;
        this.monitor = monitor;
    }
    
    // Producimos productos y los almacenamos
    public void run() {
        while(true) {
            Producto[] productos = new Producto[pid];
            // Producimos una lista de pid elementos, todos con valor pid
            for (int i = 0; i < pid; ++i) {
                productos[i] = new Producto(pid);
            }
            monitor.almacenar(productos);
        }
    }
}

class Consumidor implements Runnable {
    private int pid;
    private MonitorMultiBuffer monitor;
    
    public Consumidor(int pid, MonitorMultiBuffer monitor) {
        this.pid = pid;
        this.monitor = monitor;
    }
    
    // Extraemos productos y los consumimos
    public void run() {
        while(true) {
            // Extraemos una lista de pid elementos
            monitor.extraer(pid);
        }
    }
}
