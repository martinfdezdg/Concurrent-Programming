// Parte 1:
// Evitar con semáforos una condición de carrera.

package parte1;

import java.util.concurrent.Semaphore;

public class Carrera {
    // N, M, variable
    public static final int N = 100, M = 10;
    public static int var = 0;
    
    public static void main(String args[]) {
        Thread[] hilos = new Thread[2*M];
        
        Semaphore sem = new Semaphore(1);

        // Creamos M procesos de incremento
        for (int i = 0; i < M; ++i) {
            hilos[i] = new Thread(new Incrementador(sem));
            hilos[i].start();
        }
        
        // Creamos M procesos de decremento
        for (int i = M; i < 2*M; ++i) {
            hilos[i] = new Thread(new Decrementador(sem));
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
    private static Semaphore sem;
    
    public Incrementador(Semaphore sem) {
        this.sem = sem;
    }
    
    // Incrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            try {
                sem.acquire();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            Carrera.var++;
            sem.release();
        }
    }
}

class Decrementador implements Runnable {
    private static Semaphore sem;
    
    public Decrementador(Semaphore sem) {
        this.sem = sem;
    }
    
    // Decrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            try {
                sem.acquire();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            Carrera.var--;
            sem.release();
        }
    }
}
