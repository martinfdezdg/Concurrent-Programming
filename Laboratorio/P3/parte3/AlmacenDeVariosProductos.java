package parte3;

import java.util.concurrent.Semaphore;

public class AlmacenDeVariosProductos implements Almacen {
    private final int CAPACIDAD = 2;
    private Semaphore vacio = new Semaphore(CAPACIDAD), lleno = new Semaphore(0);
    private Semaphore mutexP = new Semaphore(1), mutexC = new Semaphore(1);
    private volatile Producto[] productos = new Producto[CAPACIDAD];
    private int ini = 0, fin = 0;
    
    public void almacenar(Producto producto) {
        try {
            vacio.acquire();
            mutexP.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productos[fin] = producto;
        fin = (fin + 1) % CAPACIDAD;
        System.out.println("+ Producto " + producto.getPid() + " almacenado");
        mutexP.release();
        lleno.release();
    }

    public Producto extraer() {
        try {
            lleno.acquire();
            mutexC.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Producto producto = productos[ini];
        ini = (ini + 1) % CAPACIDAD;
        System.out.println("- Producto " + producto.getPid() + " consumido");
        mutexC.release();
        vacio.release();
        
        return producto;
    }
}
