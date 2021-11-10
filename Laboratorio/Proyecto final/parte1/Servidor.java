package parte1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class Servidor {
    
    public static void main(String[] args) throws IOException {
        // Guardamos el puerto asignado a la comunicacion
        if (args.length < 1) {
            System.err.println("  [Indica un puerto de comunicacion]");
            System.exit(1);
        }
        int puerto = Integer.parseInt(args[0]);
        
        System.out.println("  SERVIDOR DE FICHEROS");
        
        // Creamos el servidor
        ServerSocket serverSocket = new ServerSocket(puerto);
        
        while (true) {
            // Esperamos a un cliente
            Socket socket = serverSocket.accept();
            System.out.println("+ Cliente conectado");
            
            // Creamos y ejecutamos un proceso canal de comunicacion
            OyenteCliente oyenteCliente = new OyenteCliente(socket);
            oyenteCliente.start();
        }
    }
    
}
