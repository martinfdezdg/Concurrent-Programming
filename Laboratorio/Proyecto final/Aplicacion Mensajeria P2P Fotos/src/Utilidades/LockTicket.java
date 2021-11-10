// Algoritmo Ticket

package src.Utilidades;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements LockPropio {
    private int numProcesses;
    private volatile AtomicInteger number;
    private volatile int next;
    private volatile int[] turn;
    
    public LockTicket(int numProcesses) {
        this.numProcesses = numProcesses;
        
        // AtomicInteger para ejecutar operaciones atomicas en takeLock()
        number = new AtomicInteger(0);
        next = 0;
        turn = new int[numProcesses];
    }

    public void takeLock(int i) {
        // Coge turno y actualiza number al turno siguiente atomicamente
        turn[i] = number.getAndAdd(1);
        turn = turn;
        if (turn[i] == numProcesses) number.getAndAdd(-numProcesses);
        if (turn[i] >= numProcesses) turn[i] = turn[i] - numProcesses;
        turn = turn;
        // Espera activa de su turno
        while (turn[i] != next);
    }

    public void releaseLock(){
        next = (next + 1) % numProcesses;
    }
}
