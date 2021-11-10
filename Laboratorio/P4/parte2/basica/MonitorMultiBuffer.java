package basica;

public class MonitorMultiBuffer {
    private final int CAPACIDAD = 10;
    private volatile Producto[] productos = new Producto[CAPACIDAD];
    private int ini = 0, fin = 0, numProductos = 0;
    
	public synchronized void almacenar(Producto[] almacenados) {
        while (CAPACIDAD - this.numProductos < almacenados.length) {
            try {
                wait();
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
        
        notifyAll();
	}
    
	public synchronized Producto[] extraer(int numExtraidos) {
        while (this.numProductos < numExtraidos) {
            try {
                wait();
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
        
        notifyAll();
        
        return extraidos;
	}
}
