// Parte 1: Creación de procesos (threads)

package parte1;

public class Creador {
    // N procesos
    private static final int N = 10;
    
    public static void main(String args[]) {
        Thread[] hilos = new Thread[N];
        Hilo hilo = new Hilo();
        
        for (int i = 0; i < N; ++i) {
            hilos[i] = new Thread(hilo);
            hilos[i].start();
        }
        
        for (int i = 0; i < N; ++i) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Muestro el identificador del hilo
        System.out.println("- Todos los hilos han terminado su ejecución");
    }
}

class Hilo implements Runnable {
    // T tiempo de espera
    private static final int T = 1000;
    
    public void run() {
        // Muestro el identificador del hilo
        System.out.println("> Inicio del hilo " + Thread.currentThread().getId());
        
        // Duermo T milisegundos
        try {
            Thread.sleep(T);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Muestro el identificador del hilo otra vez
        System.out.println("< Fin del hilo " + Thread.currentThread().getId());
    }
}
