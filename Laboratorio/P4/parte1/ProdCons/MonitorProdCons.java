package ProdCons;

public class MonitorProdCons {
    private final int CAPACIDAD = 5;
    private volatile Producto[] productos = new Producto[CAPACIDAD];
    private int ini = 0, fin = 0, numProductos = 0;
    
	public synchronized void almacenar(Producto producto) {
        while (numProductos == CAPACIDAD) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        productos[fin] = producto;
        productos = productos;
        
        numProductos++;
        System.out.println("+ Producto " + producto.getPid() + " almacenado");
        System.out.print("[ Productos almacenados (" + numProductos + "): ");
        int i = ini;
        while (i != fin) {
            System.out.print(productos[i].getPid()+", ");
            i = (i + 1) % CAPACIDAD;
        }
        System.out.println(productos[fin].getPid() + " ]");
        fin = (fin + 1) % CAPACIDAD;
        
        notifyAll();
	}
	public synchronized Producto extraer() {
        while (numProductos == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Producto producto = productos[ini];
        ini = (ini + 1) % CAPACIDAD;
        numProductos--;
        System.out.println("- Producto " + producto.getPid() + " consumido");
        notifyAll();
        
        return producto;
	}
}
