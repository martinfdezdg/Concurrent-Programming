package src.Cliente;

import src.Interfaz.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.nio.ByteBuffer;

public class Receptor extends Thread {
    private String id_receptor;
    private String id_fichero;
    private String ip_emisor;
    private int puerto;
    
    // Estructuras de proteccion de concurrencia
    private Semaphore sem_receptor;

    public Receptor(String id_receptor, String id_fichero, String ip_emisor, int puerto, Semaphore sem_receptor) {
        this.id_receptor = id_receptor;
        this.id_fichero = id_fichero;
        this.ip_emisor = ip_emisor;
        this.puerto = puerto;
        this.sem_receptor = sem_receptor;
    }
    
    public void run() {
        try {
            Socket canal = new Socket(ip_emisor,puerto);
            
            DataInputStream flujo_entrada_datos = new DataInputStream(canal.getInputStream());
            FileOutputStream flujo_escritura_datos = new FileOutputStream("test/" + id_receptor + "/" + id_fichero);
            
            // Obtenemos el peso de la foto
            int num_bytes = flujo_entrada_datos.readInt();
            // Indicamos el peso de cada paquete
            byte[] buffer = new byte[512];
            
            int leido = 0;
            int total_leido = 0;
            int restante = num_bytes;
            while ((leido = flujo_entrada_datos.read(buffer,0,Math.min(buffer.length,restante))) > 0) {
                total_leido += leido;
                restante -= leido;
                GUI.actualiza_progreso(total_leido,num_bytes);
                flujo_escritura_datos.write(buffer,0,leido);
            }
            GUI.println();
            
            flujo_escritura_datos.close();
            flujo_entrada_datos.close();
            
            canal.close();
            sem_receptor.release();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
