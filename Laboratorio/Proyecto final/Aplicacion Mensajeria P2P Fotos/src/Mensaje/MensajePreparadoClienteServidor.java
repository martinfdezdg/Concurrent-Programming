package src.Mensaje;

public class MensajePreparadoClienteServidor extends Mensaje {
    private String id_fichero, id_receptor;
    private int puerto;
    
    public MensajePreparadoClienteServidor(String id_fichero, String id_receptor, int puerto, String origen, String destino) {
        super(origen,destino);
        this.id_fichero = id_fichero;
        this.id_receptor = id_receptor;
        this.puerto = puerto;
    }
    
    public String getIdFichero() {
        return id_fichero;
    }
    public String getIdReceptor() {
        return id_receptor;
    }
    public int getPuerto() {
        return puerto;
    }
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_PREPARADO_CLIENTE_SERVIDOR;
    }
}
