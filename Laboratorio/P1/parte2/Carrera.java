// Parte 2: Provocar una condición de carrera

package parte2;

public class Carrera {
    // N, M, variable
    public static final int N = 1000, M = 1000;
    public static int var = 0;
    
    public static void main(String args[]) {
        Thread[] hilos = new Thread[2*M];
        Incrementador hiloInc = new Incrementador();
        Decrementador hiloDec = new Decrementador();
        
        // Creamos M procesos de incremento
        for (int i = 0; i < M; ++i) {
            hilos[i] = new Thread(hiloInc);
            hilos[i].start();
        }
        
        // Creamos M procesos de decremento
        for (int i = M; i < 2*M; ++i) {
            hilos[i] = new Thread(hiloDec);
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
    // Incrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            Carrera.var++;
        }
    }
}

class Decrementador implements Runnable {
    // Decrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            Carrera.var--;
        }
    }
}
