package src.Utilidades;

import java.io.*;
import java.util.List;

public class Usuario implements Serializable {
    String id;
    String ip;
    String ip_servidor;
    int puerto;
    List<String> id_ficheros;
    
    public Usuario(String id, String ip, String ip_servidor, int puerto, List<String> id_ficheros) {
        this.id = id;
        this.ip = ip;
        this.ip_servidor = ip_servidor;
        this.puerto = puerto;
        this.id_ficheros = id_ficheros;
    }
    
    public void addIdFichero(String id_fichero) {
        if (!id_ficheros.contains(id_fichero)) id_ficheros.add(id_fichero);
    }
    public String getId() {
        return id;
    }
    public String getIp() {
        return ip;
    }
    public String getIpServidor() {
        return ip_servidor;
    }
    public int getPuerto() {
        return puerto;
    }
    public List<String> getIdFicheros() {
        return id_ficheros;
    }
}
