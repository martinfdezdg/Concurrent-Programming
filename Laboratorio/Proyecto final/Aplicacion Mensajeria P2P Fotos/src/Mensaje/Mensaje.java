package src.Mensaje;

import java.io.*;

public abstract class Mensaje implements Serializable {
    private String origen, destino;
    
    public Mensaje(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }
    
    public abstract TipoMensaje getTipo();
    public String getOrigen() {
        return origen;
    }
    public String getDestino() {
        return destino;
    }
}
