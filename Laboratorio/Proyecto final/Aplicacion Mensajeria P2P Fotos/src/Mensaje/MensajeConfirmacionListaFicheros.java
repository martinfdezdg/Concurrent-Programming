package src.Mensaje;

import java.util.*;

public class MensajeConfirmacionListaFicheros extends Mensaje {
    private List<String> id_ficheros;
    
    public MensajeConfirmacionListaFicheros(List<String> id_ficheros, String origen, String destino) {
        super(origen,destino);
        this.id_ficheros = id_ficheros;
    }
    
    public List<String> getIdFicheros() {
        return id_ficheros;
    }
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_CONFIRMACION_LISTA_FICHEROS;
    }
}
