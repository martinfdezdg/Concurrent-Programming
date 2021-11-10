// Parte 1:
// Evitar con exclusión mutua una condición de carrera entre dos procesos.
// Usando el algoritmo Rompe-empate.

package parte1;

public class Carrera {
    // N, M, variable
    // M = 1 => 2 procesos
    public static final int N = 1000, M = 1;
    public static int var = 0;
    
    public static void main(String args[]) {
        Thread[] hilos = new Thread[2*M];
        
        RompeEmpate re = new RompeEmpate();
        
        Incrementador hiloInc = new Incrementador(re);
        Decrementador hiloDec = new Decrementador(re);
        
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
    private volatile RompeEmpate re;
    
    public Incrementador(RompeEmpate re) {
        this.re = re;
    }
    
    // Incrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            re.lock(0);
            Carrera.var++;
            re.unlock(0);
        }
    }
}

class Decrementador implements Runnable {
    private volatile RompeEmpate re;
    
    public Decrementador(RompeEmpate re) {
        this.re = re;
    }
    
    // Decrementamos el valor de la variable compartida N veces
    public void run() {
        for (int i = 0; i < Carrera.N; ++i) {
            re.lock(1);
            Carrera.var--;
            re.unlock(1);
        }
    }
}
