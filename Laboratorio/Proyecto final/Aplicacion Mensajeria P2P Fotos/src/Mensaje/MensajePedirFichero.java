package src.Mensaje;

public class MensajePedirFichero extends Mensaje {
    private String id_fichero;
    
    public MensajePedirFichero(String id_fichero, String origen, String destino) {
        super(origen,destino);
        this.id_fichero = id_fichero;
    }
    
    public String getIdFichero() {
        return id_fichero;
    }
    public TipoMensaje getTipo() {
        return TipoMensaje.MENSAJE_PEDIR_FICHERO;
    }
}
