package src.Servidor;

import src.Mensaje.*;
import src.Interfaz.*;
import src.Utilidades.*;
import java.io.*;
import java.net.Socket;

public class OyenteCliente extends Thread {
    private Servidor servidor;
    private Usuario usuario;
    private Socket canal;
    private int numero_cliente;
    
    public OyenteCliente(Servidor servidor, Socket canal, int numero_cliente) {
        super();
        this.servidor = servidor;
        this.canal = canal;
        this.numero_cliente = numero_cliente;
    }
    
    public void run() {
        boolean fin = false;
        
        try {
            ObjectInputStream flujo_entrada = new ObjectInputStream(canal.getInputStream());
            ObjectOutputStream flujo_salida = new ObjectOutputStream(canal.getOutputStream());
            
            while (!fin) {
                // Leemos el mensaje enviado por el cliente
                Mensaje mensaje_cliente = (Mensaje) flujo_entrada.readObject();
                
                switch (mensaje_cliente.getTipo()) {
                        
                    case MENSAJE_CONEXION:
                        MensajeConexion conexion = (MensajeConexion) mensaje_cliente;
                        
                        usuario = conexion.getUsuario();
                        servidor.addUsuario(usuario,flujo_entrada,flujo_salida);
                        
                        flujo_salida.writeObject(new MensajeConfirmacionConexion("Servidor",usuario.getId()));
                        GUI.printNotificacion("[Conexion]","Cliente " + usuario.getId());
                        break;
                        
                    case MENSAJE_LISTA_USUARIOS:
                        flujo_salida.writeObject(new MensajeConfirmacionListaUsuarios(servidor.getIdUsuariosConectados(),"Servidor",usuario.getId()));
                        GUI.printNotificacion("[Emision]",usuario.getId() + " ha solicitado la lista de usuarios conectados");
                        break;
                        
                    case MENSAJE_LISTA_FICHEROS:
                        flujo_salida.writeObject(new MensajeConfirmacionListaFicheros(servidor.getIdFicherosDisponibles(),"Servidor",usuario.getId()));
                        GUI.printNotificacion("[Emision]",usuario.getId() + " ha solicitado la lista de fotos");
                        break;
                        
                    case MENSAJE_PEDIR_FICHERO:
                        MensajePedirFichero pedir_fichero = (MensajePedirFichero) mensaje_cliente;
                        ObjectOutputStream flujo_salida_emisor = servidor.getFlujoSalidaUsuario(usuario.getId(),pedir_fichero.getIdFichero());
                        
                        if (flujo_salida_emisor != null) {
                            int puerto = servidor.getPuerto(numero_cliente);
                            usuario.addIdFichero(pedir_fichero.getIdFichero());
                            servidor.updateUsuario(usuario,pedir_fichero.getIdFichero());
                            flujo_salida_emisor.writeObject(new MensajeEmitirFichero(pedir_fichero.getIdFichero(),usuario.getId(),puerto,"Servidor",usuario.getId()));
                        }
                        else flujo_salida.writeObject(new MensajeFicheroNoExistente("Servidor",usuario.getId()));
                        GUI.printNotificacion("[Emision]",usuario.getId() + " ha solicitado el fichero " + pedir_fichero.getIdFichero());
                        break;
                        
                    case MENSAJE_PREPARADO_CLIENTE_SERVIDOR:
                        MensajePreparadoClienteServidor preparado_cliente_servidor = (MensajePreparadoClienteServidor) mensaje_cliente;
                        String id_fichero = preparado_cliente_servidor.getIdFichero();
                        int puerto = preparado_cliente_servidor.getPuerto();
                        String id_receptor = preparado_cliente_servidor.getIdReceptor();
                        ObjectOutputStream flujo_salida_receptor = servidor.getFlujoSalidaUsuario(usuario.getId(),preparado_cliente_servidor.getIdReceptor());
                        
                        flujo_salida_receptor.writeObject(new MensajePreparadoServidorCliente(id_fichero,usuario.getIp(),puerto,"Servidor",id_receptor));
                        GUI.printNotificacion("[P2P]","Todo preparado para la trasnferencia del fichero");
                        break;
                    
                    case MENSAJE_CERRAR_CONEXION:
                        servidor.removeUsuario(usuario.getId());
                        
                        flujo_salida.writeObject(new MensajeConfirmacionCerrarConexion("Servidor",usuario.getId()));
                        GUI.printNotificacion("[Desconexion]","Cliente " + usuario.getId());
                        flujo_entrada.close();
                        flujo_salida.close();
                        fin = true;
                        
                    default: break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
