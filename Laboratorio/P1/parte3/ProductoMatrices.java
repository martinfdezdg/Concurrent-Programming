// Parte 3: Multiplicación de matrices por N threads.

package parte3;

public class ProductoMatrices {
    // Declaración de las matrices iniciales
    public static final int N = 3;
    public static final int[][] A = {{1,2,3},{4,5,6},{7,8,9}};
    public static final int[][] B = {{9,8,7},{6,5,4},{3,2,1}};
    public static int[][] C = new int[N][N];
    
    public static void main(String args[]) {
        Thread[] hilos = new Thread[N];
        
        // Creamos N hilos para hallar las N filas
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j){
                hilos[i] = new Thread(new CalcularFila(j));
                hilos[i].start();
            }
        }
        
        for (int i = 0; i < N; ++i) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Muestro las matrices iniciales
        System.out.println("> A = ");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                System.out.print(A[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        
        System.out.println("> B = ");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                System.out.print(B[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        
        // Muestro la matriz producto
        System.out.println("> El producto de A*B = ");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                System.out.print(C[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");

    }
}

class CalcularFila implements Runnable {
    private static int fila;
    
    public CalcularFila(int fila) {
        this.fila = fila;
    }
    
    // Incrementamos el valor de la variable compartida N veces
    public void run() {
        int suma;
        for (int j = 0; j < ProductoMatrices.N; ++j) {
            suma = 0;
            for (int k = 0; k < ProductoMatrices.N; ++k) {
                suma += ProductoMatrices.A[fila][k] * ProductoMatrices.B[k][j];
            }
            ProductoMatrices.C[fila][j] = suma;
        }
    }
}
