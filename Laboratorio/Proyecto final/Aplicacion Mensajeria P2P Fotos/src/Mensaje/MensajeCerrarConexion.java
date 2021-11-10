package src.Mensaje;

public class MensajeCerrarConexion extends Mensaje {
    
    public MensajeCerrarConexion(String origen, String destino) {
        super(origen,destino);
    }
    
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_CERRAR_CONEXION;
    }
}
