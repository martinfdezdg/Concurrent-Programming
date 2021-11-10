package src.Mensaje;

public class MensajeConfirmacionCerrarConexion extends Mensaje {
    
    public MensajeConfirmacionCerrarConexion(String origen, String destino) {
        super(origen,destino);
    }
    
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_CONFIRMACION_CERRAR_CONEXION;
    }
}
