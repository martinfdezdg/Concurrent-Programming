package src.Mensaje;

import src.Utilidades.*;

public class MensajeConexion extends Mensaje {
    private Usuario usuario;
    
    public MensajeConexion(Usuario usuario, String origen, String destino) {
        super(origen,destino);
        this.usuario = usuario;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_CONEXION;
    }
}
