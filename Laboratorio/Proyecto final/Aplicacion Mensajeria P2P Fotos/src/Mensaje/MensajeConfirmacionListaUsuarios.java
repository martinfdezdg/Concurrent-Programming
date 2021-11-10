package src.Mensaje;

import java.util.*;

public class MensajeConfirmacionListaUsuarios extends Mensaje {
    private List<String> id_usuarios_conectados;
    
    public MensajeConfirmacionListaUsuarios(List<String> id_usuarios_conectados, String origen, String destino) {
        super(origen,destino);
        this.id_usuarios_conectados = id_usuarios_conectados;
    }
    
    public List<String> getIdUsuariosConectados() {
        return id_usuarios_conectados;
    }
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_CONFIRMACION_LISTA_USUARIOS;
    }
}
