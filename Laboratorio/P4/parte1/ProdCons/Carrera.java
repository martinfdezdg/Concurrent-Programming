package ProdCons;

public class Carrera {
    // Numero de hilos productores y consumidores
    public static final int N_PROD = 1000;
    public static final int N_CONS = 1000;
    // Creacion del almacen
    private static MonitorProdCons monitor = new MonitorProdCons();
    
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
            consumidores[i] = new Thread(new Consumidor(monitor));
            consumidores[i].start();
        }
    }
}

class Productor implements Runnable {
    private int pid;
    private MonitorProdCons monitor;
    
    public Productor(int pid, MonitorProdCons monitor) {
        this.pid = pid;
        this.monitor = monitor;
    }
    
    // Producimos un producto y lo almacenamos
    public void run() {
        while(true) {
            Producto producto = new Producto(pid);
            monitor.almacenar(producto);
        }
    }
}

class Consumidor implements Runnable {
    private MonitorProdCons monitor;
    
    public Consumidor(MonitorProdCons monitor) {
        this.monitor = monitor;
    }
    
    // Extraemos un producto y lo consumimos
    public void run() {
        while(true) {
            monitor.extraer();
        }
    }
}
