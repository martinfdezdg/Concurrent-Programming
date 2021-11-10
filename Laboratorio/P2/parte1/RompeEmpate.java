// Algoritmo Rompe-Empate

package parte1;

public class RompeEmpate {
    private volatile boolean[] in = {false,false};
    private volatile int last;

    public void lock(int i) {
        // El hilo i quiere entrar a la SC
        in[i] = true;
        // El hilo i es el último en llegar a la SC
        last = i;
        // Mientras el otro proceso (1-i) quiera entrar y el último en llegar al SC sea este (i):
        // Espera activa
        while (in[1-i] && last == i);
    }

    public void unlock(int i){
        // El hilo i ya no quiere entrar a la SC
        in[i] = false;
    }
}
