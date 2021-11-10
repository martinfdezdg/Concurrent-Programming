package src.Mensaje;

import java.net.*;

public class MensajePreparadoServidorCliente extends Mensaje {
    private String id_fichero;
    private String ip_emisor;
    private int puerto;
    
    public MensajePreparadoServidorCliente(String id_fichero, String ip_emisor, int puerto, String origen, String destino) {
        super(origen,destino);
        this.id_fichero = id_fichero;
        this.ip_emisor = ip_emisor;
        this.puerto = puerto;
    }
    
    public String getIdFichero() {
        return id_fichero;
    }
    public String getIpEmisor() {
        return ip_emisor;
    }
    public int getPuerto() {
        return puerto;
    }
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_PREPARADO_SERVIDOR_CLIENTE;
    }
}
