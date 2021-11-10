package src.Cliente;

import src.Mensaje.*;
import src.Interfaz.*;
import src.Utilidades.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.net.*;

public class OyenteServidor extends Thread {
    private Cliente cliente;
    private ObjectInputStream flujo_entrada;
    private ObjectOutputStream flujo_salida;
    
    // Estructuras de proteccion de concurrencia
    private Semaphore sem_mensajes;
    private Semaphore sem_receptor = new Semaphore(0);
    
    public OyenteServidor(Cliente cliente, ObjectInputStream flujo_entrada, ObjectOutputStream flujo_salida, Semaphore sem_mensajes) {
        super();
        this.cliente = cliente;
        this.flujo_entrada = flujo_entrada;
        this.flujo_salida = flujo_salida;
        this.sem_mensajes = sem_mensajes;
    }
    
    public void run() {
        boolean fin = false;
        
        try {
            while (!fin) {
                // Leemos el mensaje enviado por el servidor
                Mensaje mensaje_cliente = (Mensaje) flujo_entrada.readObject();
                
                switch (mensaje_cliente.getTipo()) {
                        
                    case MENSAJE_CONFIRMACION_CONEXION:
                        GUI.printNotificacion("[Conexion]","Conexion establecida con el servidor");
                        GUI.println();
                        sem_mensajes.release();
                        break;
                        
                    case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
                        MensajeConfirmacionListaUsuarios confirmacion_lista_usuarios = (MensajeConfirmacionListaUsuarios) mensaje_cliente;
                        List<String> id_usuarios_conectados = confirmacion_lista_usuarios.getIdUsuariosConectados();
                        
                        GUI.printNotificacion("[Recepcion]","Lista de usuarios conectados");
                        GUI.println();
                        GUI.println("Usuarios conectados");
                        for (String id_usuario : id_usuarios_conectados) {
                            GUI.printIdUsuarioConectado(id_usuario);
                        }
                        GUI.println();
                        sem_mensajes.release();
                        break;
                        
                    case MENSAJE_CONFIRMACION_LISTA_FICHEROS:
                        MensajeConfirmacionListaFicheros confirmacion_lista_ficheros = (MensajeConfirmacionListaFicheros) mensaje_cliente;
                        List<String> id_ficheros = confirmacion_lista_ficheros.getIdFicheros();
                        
                        GUI.printNotificacion("[Recepcion]","Lista de fotos");
                        GUI.println();
                        GUI.println("Fotos");
                        if (id_ficheros.isEmpty()) {
                            GUI.printNotificacion("No hay fotos");
                        }
                        else {
                            for (String id_fichero : id_ficheros) {
                                GUI.printIdFichero(id_fichero);
                            }
                        }
                        GUI.println();
                        sem_mensajes.release();
                        break;
                        
                    case MENSAJE_EMITIR_FICHERO:
                        MensajeEmitirFichero emitir_fichero = (MensajeEmitirFichero) mensaje_cliente;
                        String id_emisor = cliente.getIdUsuario();
                        String id_fichero = emitir_fichero.getIdFichero();
                        int puerto = emitir_fichero.getPuerto();
                        
                        Emisor emisor = new Emisor(id_emisor,id_fichero,puerto);
                        emisor.start();
                        
                        flujo_salida.writeObject(new MensajePreparadoClienteServidor(id_fichero,emitir_fichero.getIdUsuarioReceptor(),puerto,cliente.getIdUsuario(),id_emisor));
 
                        GUI.printNotificacion("\nFichero emitido");
                        GUI.print("> "); // Lo mas probable es que este en el menu mientras emite un fichero
                        break;
                        
                    case MENSAJE_FICHERO_NO_EXISTENTE:
                        GUI.printNotificacion("Fichero no existente");
                        GUI.println();
                        
                        sem_mensajes.release();
                        break;
                        
                    case MENSAJE_PREPARADO_SERVIDOR_CLIENTE:
                        MensajePreparadoServidorCliente preparado_servidor_cliente = (MensajePreparadoServidorCliente) mensaje_cliente;
                        String id_receptor = cliente.getIdUsuario();
                        String ip_emisor = preparado_servidor_cliente.getIpEmisor();
                        
                        Receptor receptor = new Receptor(id_receptor,preparado_servidor_cliente.getIdFichero(),ip_emisor,preparado_servidor_cliente.getPuerto(),sem_receptor);
                        receptor.start();
                        try {
                            sem_receptor.acquire();
                        }  catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        GUI.printNotificacion("Fichero recibido");
                        GUI.println();
                        
                        sem_mensajes.release();
                        break;
                        
                    case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
                        GUI.printNotificacion("[Desconexion]","Conexion cerrada con el servidor");
                        sem_mensajes.release();
                        flujo_entrada.close();
                        flujo_salida.close();
                        fin = true;
                        break;
                        
                    default: break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
