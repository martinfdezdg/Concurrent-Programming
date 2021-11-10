package parte2;

import java.util.concurrent.Semaphore;

public class AlmacenDeUnProducto implements Almacen {
    private Semaphore vacio = new Semaphore(1);
    private Semaphore lleno = new Semaphore(0);
    private volatile Producto producto;
    
    public void almacenar(Producto producto) {
        try {
            vacio.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.producto = producto;
        System.out.println("+ Producto " + producto.getPid() + " almacenado");
        lleno.release();
    }

    public Producto extraer() {
        try {
            lleno.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Producto producto = this.producto;
        System.out.println("- Producto " + producto.getPid() + " consumido");
        vacio.release();
        
        return producto;
    }
}
