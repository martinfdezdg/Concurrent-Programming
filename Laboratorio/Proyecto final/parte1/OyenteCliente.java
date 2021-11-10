package parte1;

import java.net.Socket;
import java.io.*;

public class OyenteCliente extends Thread {
    private Socket socket;
    
    public OyenteCliente(Socket socket) {
        super();
        this.socket = socket;
    }
    
    public void run() {
        try {
            // Creamos los flujos de lectura/escritura
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(),true);
            
            // Recibimos el nombre del fichero que quiere el cliente
            
            String ruta = "parte1/ficheros/";
            String nombreFichero = lector.readLine();
            System.out.println("  [Fichero " + nombreFichero + " solicitado por el cliente]");
            
            try {
                // Buscamos el fichero
                BufferedReader lecturaFichero = new BufferedReader(new InputStreamReader(new FileInputStream(ruta+nombreFichero)));
                System.out.println("  [Fichero " + nombreFichero + " encontrado]");
                escritor.println("FOUND");
                
                // Enviamos el fichero al cliente linea a linea
                String linea = lecturaFichero.readLine();
                while (linea != null) {
                    escritor.write(linea + "\n");
                    linea = lecturaFichero.readLine();
                }
                escritor.flush();
                lecturaFichero.close();
                
            } catch (FileNotFoundException e) {
                escritor.println("NOT FOUND");
                System.err.println("  [Fichero " + nombreFichero + " no encontrado]");
            } finally {
                // Cerramos los flujos de lectura/escritura
                lector.close();
                escritor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
