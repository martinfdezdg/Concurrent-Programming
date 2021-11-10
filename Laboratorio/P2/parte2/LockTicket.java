// Algoritmo Ticket

package parte2;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements Lock{
    private int numProcesses;
    private volatile AtomicInteger number;
    private volatile int next;
    private volatile int[] turn;
    
    public LockTicket() {
        numProcesses = 2*Carrera.M;
        
        // AtomicInteger para ejecutar operaciones atómicas en takeLock()
        number = new AtomicInteger(0);
        next = 0;
        turn = new int[numProcesses];
    }

    public void takeLock(int i) {
        // Coge turno y actualiza number al turno siguiente atómicamente
        turn[i] = number.getAndAdd(1);
        turn = turn;
        // Espera activa de su turno
        while (turn[i] != next);
    }

    public void releaseLock(int i){
        next++;
    }
}
