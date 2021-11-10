package src.Cliente;

import src.Mensaje.*;
import src.Servidor.*;
import src.Interfaz.*;
import src.Utilidades.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.net.Socket;
import java.net.InetAddress;

public class Cliente extends Thread {
    private Usuario usuario;
    private Scanner terminal_entrada;
    
    // Estructuras de proteccion de concurrencia
    private Semaphore sem_mensajes = new Semaphore(0);
    
    
    public Cliente(Usuario usuario, Scanner terminal_entrada) {
        this.usuario = usuario;
        this.terminal_entrada = terminal_entrada;
    }
    
    
    public String getIdUsuario() {
        return usuario.getId();
    }
    
    
    public void printOpciones() {
        GUI.printOpcion(1,"Ver usuarios conectados");
        GUI.printOpcion(2,"Ver fotos disponibles");
        GUI.printOpcion(3,"Descargar fotos");
        GUI.printOpcion(0,"Salir");
        GUI.print("> ");
    }
    
    
    public void run() {
        try {
            // Creamos el canal de comunicacion con el servidor
            Socket canal = new Socket(usuario.getIpServidor(),usuario.getPuerto());
            
            // Creamos los flujos de lectura/escritura
            ObjectOutputStream flujo_salida = new ObjectOutputStream(canal.getOutputStream());
            ObjectInputStream flujo_entrada = new ObjectInputStream(canal.getInputStream());

            // Creamos el hilo del OyenteServidor para que lea el Socket
            OyenteServidor oyente_servidor = new OyenteServidor(this,flujo_entrada,flujo_salida,sem_mensajes);
            oyente_servidor.start();
            
            
            try {
                // Avisamos al servidor de que estamos conectados
                flujo_salida.writeObject(new MensajeConexion(usuario,usuario.getId(),"Servidor"));
                sem_mensajes.acquire();
                
                // Mostramos las opciones al cliente
                int opcion = -1;
                while (opcion != 0) {
                    printOpciones();
                    opcion = Integer.parseInt(terminal_entrada.nextLine());
                    switch (opcion) {
                            
                        case 1:
                            // Pedimos al servidor la lista de usuarios conectados
                            flujo_salida.writeObject(new MensajeListaUsuarios(usuario.getId(),"Servidor"));
                            sem_mensajes.acquire();
                            break;
                                
                        case 2:
                            // Pedimos al servidor la lista de fotos disponibles
                            flujo_salida.writeObject(new MensajeListaFicheros(usuario.getId(),"Servidor"));
                            sem_mensajes.acquire();
                            break;
                                
                        case 3:
                            GUI.print("\nFichero: ");
                            String id_fichero = terminal_entrada.nextLine();
                            // Pedimos al servidor un fichero
                            flujo_salida.writeObject(new MensajePedirFichero(id_fichero,usuario.getId(),"Servidor"));
                            sem_mensajes.acquire();
                            break;
                                
                        case 0:
                            // Pedimos al servidor que nos desconecte
                            flujo_salida.writeObject(new MensajeCerrarConexion(usuario.getId(),"Servidor"));
                            sem_mensajes.acquire();
                            break;
                                
                        default: GUI.printError("La opcion no es valida");
                    }
                }
                    
                // Cerramos el canal de comunicacion con el usuario
                terminal_entrada.close();
            }  catch(InterruptedException e) {
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
