// Parte 2:
// Evitar con exclusión mutua una condición de carrera entre 2M procesos.
// Usando el algoritmo Rompe-empate, Ticket y Bakery.

package parte2;

public class Carrera {
    // N, M, variable
    public static final int N = 100, M = 10;
    public static int var = 0;
    
    public static void main(String args[]) {
        Thread[] hilos = new Thread[2*M];
        
        // Lock lock = new LockRompeEmpate();
        Lock lock = new LockTicket();
        // Lock lock = new LockBakery();

        // Creamos M procesos de incremento
        for (int i = 0; i < M; ++i) {
            hilos[i] = new Thread(new Incrementador(i,lock));
            hilos[i].start();
        }
        
        // Creamos M procesos de decremento
        for (int i = M; i < 2*M; ++i) {
            hilos[i] = new Thread(new Decrementador(i,lock));
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
        System.out.println("> La variable var = " + var);
    }
}

class Incrementador implements Runnable {
    private int id;
    private volatile Lock lock;
    
    public Incrementador(int id, Lock lock) {
        this.id = id;
        this.lock = lock;
    }
    
    // Incrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            lock.takeLock(id);
            Carrera.var++;
            lock.releaseLock(id);
        }
    }
}

class Decrementador implements Runnable {
    private int id;
    private volatile Lock lock;
    
    public Decrementador(int id, Lock lock) {
        this.id = id;
        this.lock = lock;
    }
    
    // Decrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            lock.takeLock(id);
            Carrera.var--;
            lock.releaseLock(id);
        }
    }
}
