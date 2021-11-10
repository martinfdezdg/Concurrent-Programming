package mejorada;

import java.util.concurrent.locks.*;

public class MonitorMultiBuffer {
    private final Lock lock = new ReentrantLock();
    private final Condition condAlmacena = lock.newCondition(), condExtrae = lock.newCondition();
    private final int CAPACIDAD = 100;
    private volatile Producto[] productos = new Producto[CAPACIDAD];
    private int ini = 0, fin = 0, numProductos = 0;
    
	public void almacenar(Producto[] almacenados) {
        // Tomo el lock para comprobar la condicion de almacenar y modificar variables globales
        lock.lock();
        while (CAPACIDAD - this.numProductos < almacenados.length) {
            try {
                // Espero si no hay sitio en el almacen
                condAlmacena.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Producto producto : almacenados) {
            this.productos[fin] = producto;
            fin = (fin + 1) % CAPACIDAD;
        }
        this.numProductos += almacenados.length;
        this.productos = this.productos;
        
        System.out.print("+ Productos");
        for (Producto producto : almacenados) {
            System.out.print(" " + producto.getPid());
        }
        System.out.println(" almacenados, en total hay " + numProductos + " productos");
        
        // Aviso de que he almacenado productos en el almacen
        // (no sabemos si suficientes, por eso tenemos un while)
        condExtrae.signalAll();
        // Desbloqueamos el lock
        lock.unlock();
	}
    
	public Producto[] extraer(int numExtraidos) {
        // Tomo el lock para comprobar la condicion de extraer y modificar variables globales
        lock.lock();
        while (this.numProductos < numExtraidos) {
            try {
                // Espero si no hay productos suficientes en el almacen
                condExtrae.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Producto[] extraidos = new Producto[numExtraidos];
        for (int i = 0; i < numExtraidos; ++i) {
            extraidos[i] = this.productos[ini];
            ini = (ini + 1) % CAPACIDAD;
        }
        this.numProductos -= numExtraidos;
        
        System.out.print("- Productos");
        for (Producto producto : extraidos) {
            System.out.print(" " + producto.getPid());
        }
        System.out.println(" extraidos, en total hay " + numProductos + " productos");
        
        // Aviso de que he extraido productos del almacen
        // (no sabemos si suficientes, por eso tenemos un while)
        condAlmacena.signalAll();
        // Desbloqueamos el lock
        lock.unlock();
        
        return extraidos;
	}
}
