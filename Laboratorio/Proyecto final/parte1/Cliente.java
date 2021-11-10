package parte1;

import java.io.*;
import java.util.Scanner;
import java.net.Socket;
import java.net.InetAddress;

public class Cliente {
    public static void main(String[] args) throws IOException {
        // Guardamos el puerto asignado a la comunicacion
        if (args.length < 1) {
            System.err.println("  [Indica un puerto de comunicacion]");
            System.exit(1);
        }
        int puerto = Integer.parseInt(args[0]);
        
        System.out.println("  RECEPCION DE FICHEROS");
        
        // Creamos el canal de comunicacion con el servidor y los flujos de lectura/escritura
        Socket socket = new Socket(InetAddress.getLocalHost(),puerto);
        BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter escritor = new PrintWriter(socket.getOutputStream(),true);
        
        // Pedimos el nombre del fichero
        System.out.print("> Nombre del fichero: ");
        Scanner scanner = new Scanner(System.in);
        String nombreFichero = scanner.nextLine();
        scanner.close();
        
        // Solicitamos el fichero
        escritor.println(nombreFichero);
        System.out.println("  [Fichero " + nombreFichero + " solicitado al servidor]");
        if (lector.readLine().equals("FOUND")) System.out.println("  [Fichero " + nombreFichero + " encontrado]");
        else {
            System.err.println("  [Fichero " + nombreFichero + " no encontrado]");
            System.exit(1);
        }
        
        // Recibimos el fichero
        String fichero = "";
        String linea = lector.readLine();
        while (linea != null) {
            fichero += "  " + linea;
            linea = lector.readLine();
            if (linea != null) fichero += "\n";
        };
        System.out.println("  [Fichero " + nombreFichero + " recibido]");
        System.out.println("+ Fichero:\n" + fichero);
        
        // Guardamos el fichero
        // TO-DO
        
        // Cerramos el canal de comunicacion con el servidor y los flujos de lectura/escritura
        lector.close();
        escritor.close();
        socket.close();
    }
}
