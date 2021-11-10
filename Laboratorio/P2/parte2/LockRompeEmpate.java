// Algoritmo Rompe-Empate

package parte2;

public class LockRompeEmpate implements Lock {
    private int numProcesses;
    private volatile int[] in;
    private volatile int[] last;
    
    public LockRompeEmpate() {
        numProcesses = 2*Carrera.M;
        
        in = new int[numProcesses];
        last = new int[numProcesses];
        
        for (int i = 0; i < numProcesses; ++i){
            in[i] = -1;
            last[i] = -1;
        }
    }

    public void takeLock(int i) {
        for (int j = 0; j < numProcesses; ++j){
            // El hilo i se coloca en la etapa j para entrar al SC
            in[i] = j;
            in = in; // Para actualizar el volatile array
            // El hilo i es el último en llegar de la etapa j
            last[j] = i;
            last = last; // Para actualizar el volatile array
            // Mientras el resto de procesos esten en etapas superiores y el último sea este:
            // Espera activa
            for (int k = 0; k < numProcesses; ++k){
                if (k != i) {
                    while (in[k] >= in[i] && last[j] == i);
                }
            }
        }
            
    }

    public void releaseLock(int i){
        // El hilo i vuelve a la etapa inicial
        in[i] = -1;
        in = in; // Para actualizar el volatile array
    }
}
