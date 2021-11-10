package src.Servidor;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class ControladorRW {
    private int numero_escritores = 0, numero_lectores = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition puede_leer = lock.newCondition();
    private final Condition puede_escribir = lock.newCondition();
    
    public void beginRead() {
        lock.lock();
        while (numero_escritores > 0) {
            try {
                puede_leer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numero_lectores += 1;
        lock.unlock();
    }
    public void endRead() {
        lock.lock();
        numero_lectores -= 1;
        if (numero_lectores == 0) puede_escribir.signal();
        lock.unlock();
    }
    public void beginWrite() {
        lock.lock();
        while (numero_lectores > 0 || numero_escritores > 0) {
            try {
                puede_escribir.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numero_escritores += 1;
        lock.unlock();
    }
    public void endWrite() {
        lock.lock();
        numero_escritores -= 1;
        puede_escribir.signal();
        puede_leer.signalAll();
        lock.unlock();
    }
}
