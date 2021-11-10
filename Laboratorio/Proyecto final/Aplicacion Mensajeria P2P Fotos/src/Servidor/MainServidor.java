package src.Servidor;

import src.Interfaz.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class MainServidor {
    public static void main(String[] args) throws IOException {
        // Guardamos la ip y el puerto asignado a la comunicacion
        if (args.length < 2) {
            GUI.printError("Indica una ip y un puerto de comunicacion");
            System.exit(1);
        }
        String ip = args[0];
        int puerto = Integer.parseInt(args[1]);
        
        GUI.printTitulo("MENSAJERIA P2P PARA COMPARTIR FOTOS");
        GUI.printNotificacion("Servidor iniciado en la direccion IP " + ip);
        
        Servidor servidor = new Servidor(ip,puerto);
        servidor.start();
    }
}
