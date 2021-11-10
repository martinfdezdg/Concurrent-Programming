package src.Mensaje;

public class MensajeListaFicheros extends Mensaje {
    
    public MensajeListaFicheros(String origen, String destino) {
        super(origen,destino);
    }
    
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_LISTA_FICHEROS;
    }
}
