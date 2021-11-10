package src.Mensaje;

public class MensajeFicheroNoExistente extends Mensaje {
    
    public MensajeFicheroNoExistente(String origen, String destino) {
        super(origen,destino);
    }
    
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_FICHERO_NO_EXISTENTE;
    }
}
