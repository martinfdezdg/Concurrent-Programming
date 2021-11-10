package parte3;

public class Carrera {
    // Numero de hilos productores y consumidores
    public static final int N_PROD = 10;
    public static final int N_CONS = 10;
    // Creacion del almacen
    private static Almacen almacen = new AlmacenDeVariosProductos();
    
    public static void main(String args[]) {
        Thread[] productores = new Thread[N_PROD];
        Thread[] consumidores = new Thread[N_CONS];

        // Creamos N_PROD procesos productores
        for (int i = 0; i < N_PROD; ++i) {
            productores[i] = new Thread(new Productor(i+1,almacen));
            productores[i].start();
        }
        
        // Creamos N_CONS procesos consumidores
        for (int i = 0; i < N_CONS; ++i) {
            consumidores[i] = new Thread(new Consumidor(almacen));
            consumidores[i].start();
        }
    }
}

class Productor implements Runnable {
    private int pid;
    private Almacen almacen;
    
    public Productor(int pid, Almacen almacen) {
        this.pid = pid;
        this.almacen = almacen;
    }
    
    // Producimos un producto y lo almacenamos
    public void run() {
        while(true) {
            Producto producto = new Producto(pid);
            almacen.almacenar(producto);
        }
    }
}

class Consumidor implements Runnable {
    private Almacen almacen;
    
    public Consumidor(Almacen almacen) {
        this.almacen = almacen;
    }
    
    // Extraemos un producto y lo consumimos
    public void run() {
        while(true) {
            almacen.extraer();
        }
    }
}
