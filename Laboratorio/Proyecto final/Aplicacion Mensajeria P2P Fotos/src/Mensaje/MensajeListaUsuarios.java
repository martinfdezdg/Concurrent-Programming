package src.Mensaje;

public class MensajeListaUsuarios extends Mensaje {
    
    public MensajeListaUsuarios(String origen, String destino) {
        super(origen,destino);
    }
    
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_LISTA_USUARIOS;
    }
}
