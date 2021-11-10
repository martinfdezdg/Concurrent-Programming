package src.Cliente;

import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.swing.*;

public class Emisor extends Thread {
    private String id_emisor;
    private String id_fichero;
    private int puerto;
    

    public Emisor(String id_emisor, String id_fichero, int puerto) {
        super();
        this.puerto = puerto;
        this.id_fichero = id_fichero;
        this.id_emisor = id_emisor;
        
    }
    
    public void run() {
        try {
            ServerSocket canal_emisor = new ServerSocket(puerto);
            Socket canal = canal_emisor.accept();
            
            DataOutputStream flujo_salida_datos = new DataOutputStream(canal.getOutputStream());
            FileInputStream flujo_lectura_datos = new FileInputStream("test/" + id_emisor + "/" + id_fichero);
            
            // Obtenemos el peso de la foto
            int num_bytes = (int) (new File("test/" + id_emisor + "/" + id_fichero)).length();
            // Indicamos el peso de cada paquete
            byte[] buffer = new byte[512];
            
            // Indicamos el peso de la foto al receptor
            flujo_salida_datos.writeInt(num_bytes);
            
            // Leemos la foto y la enviamos por paquetes
            while (flujo_lectura_datos.read(buffer) > 0) {
                flujo_salida_datos.write(buffer);
            }
            
            flujo_lectura_datos.close();
            flujo_salida_datos.close();

            canal_emisor.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
