package src.Cliente;

import src.Mensaje.*;
import src.Servidor.*;
import src.Interfaz.*;
import src.Utilidades.*;
import java.io.*;
import java.util.*;
import java.net.Socket;
import java.net.InetAddress;

public class MainCliente {
    
    public static void main(String[] args) throws IOException {
        // Guardamos la ip y el puerto asignado a la comunicacion
        if (args.length < 3) {
            GUI.printError("Indica tu ip y la ip y el puerto del servidor");
            System.exit(1);
        }
        String ip_cliente = args[0];
        String ip_servidor = args[1];
        int puerto = Integer.parseInt(args[2]);
        
        // Creamos el canal de comunicacion con el usuario
        Scanner terminal = new Scanner(System.in);
        
        // Informacion de inicio de sesion
        GUI.printTitulo("COMPARTIR FOTOS");
        GUI.print("Usuario: ");
        String id = terminal.nextLine();
        
        // Creamos una carpeta id_usuario donde estaran los ficheros compartidos
        String id_directorio_compartido = "test/" + id + "/";
        File directorio = new File(id_directorio_compartido);
        if (!directorio.exists()) directorio.mkdir();
        
        // Guardamos los id_fichero si ya contiene
        File[] lista_id_ficheros = directorio.listFiles();
        List<String> id_ficheros = new ArrayList<String>();
        for (int i = 0; i < lista_id_ficheros.length; i++) {
            if (!lista_id_ficheros[i].getName().equals(".DS_Store")) id_ficheros.add(lista_id_ficheros[i].getName());
        }
        
        Cliente cliente = new Cliente(new Usuario(id,ip_cliente,ip_servidor,puerto,id_ficheros),terminal);
        cliente.start();
    }
}
