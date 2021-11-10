package src.Mensaje;

public class MensajeConfirmacionConexion extends Mensaje {
    
    public MensajeConfirmacionConexion(String origen, String destino) {
        super(origen,destino);
    }
    
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_CONFIRMACION_CONEXION;
    }
}
