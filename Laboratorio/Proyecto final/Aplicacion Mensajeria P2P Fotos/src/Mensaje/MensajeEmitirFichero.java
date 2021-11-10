package src.Mensaje;

public class MensajeEmitirFichero extends Mensaje {
    private String id_fichero;
    private String id_usuario_receptor;
    private int puerto;
    
    public MensajeEmitirFichero(String id_fichero, String id_usuario_receptor, int puerto, String origen, String destino) {
        super(origen,destino);
        this.id_fichero = id_fichero;
        this.id_usuario_receptor = id_usuario_receptor;
        this.puerto = puerto;
    }
    
    public String getIdFichero() {
        return id_fichero;
    }
    public String getIdUsuarioReceptor() {
        return id_usuario_receptor;
    }
    public int getPuerto() {
        return puerto;
    }
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_EMITIR_FICHERO;
    }
}
