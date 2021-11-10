package src.Servidor;

import src.Interfaz.*;
import src.Utilidades.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.Semaphore;
import java.lang.Thread;

public class Servidor {
    private final int MAX_CLIENTES = 3;
    private String ip;
    private int puerto_servidor;
    private int puerto_p2p;
    // <Nombre de usuario,usuario>
    private Map<String,Usuario> usuarios_registrados = new HashMap<String,Usuario>();
    // <Nombre de usuario,[flujo_entrada,flujo_salida]]>
    private Map<String,List<Object>> usuarios_conectados = new HashMap<String,List<Object>>();
    // <Nombre de fichero,[nombres de usuarios]>
    private Map<String,List<String>> ficheros_disponibles = new HashMap<String,List<String>>();
    
    // Estructuras de proteccion de concurrencia
    private ControladorRW monitor_usuarios;
    private ControladorRW monitor_ficheros;
    private Semaphore sem_usuarios_registrados = new Semaphore(1);
    
    private Semaphore mutex_usuarios_registrados = new Semaphore(1);
    int numero_lectores_registro = 0;
    
    private LockPropio lock_puerto = new LockTicket(MAX_CLIENTES);
    
    
    public Servidor(String ip, int puerto_servidor) throws IOException {
        this.ip = ip;
        this.puerto_servidor = puerto_servidor;
        this.puerto_p2p = (int) (Math.random()*10000);
        this.monitor_usuarios = new ControladorRW();
        this.monitor_ficheros = new ControladorRW();
    }
    
    
    public void addUsuario(Usuario usuario, ObjectInputStream flujo_entrada, ObjectOutputStream flujo_salida) {
        // Guardamos el usuario en usuarios conectados
        // lock escritura monitorUsuarios
        monitor_usuarios.beginWrite();
        List<Object> lista_flujos = new ArrayList<Object>();
        lista_flujos.add(flujo_entrada);
        lista_flujos.add(flujo_salida);
        usuarios_conectados.put(usuario.getId(),lista_flujos);
        // unlock monitorUsuarios
        monitor_usuarios.endWrite();
        
        // Guardamos la informacion de todos los ficheros disponibles que tiene el usuario
        // lock escritura monitorFicheros
        monitor_ficheros.beginWrite();
        for (String id_fichero : usuario.getIdFicheros()) {
            if (ficheros_disponibles.get(id_fichero) == null)
                ficheros_disponibles.put(id_fichero,new ArrayList<String>());
            if (!ficheros_disponibles.get(id_fichero).contains(usuario.getId()))
                ficheros_disponibles.get(id_fichero).add(usuario.getId());
        }
        // unlock monitorFicheros
        monitor_ficheros.endWrite();
        
        // Guardamos el usuario en la base de datos
        // lock escritura semaforo
        try {
            sem_usuarios_registrados.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Sobreescribimos el usuario con la informacion que tenga ahora
        usuarios_registrados.put(usuario.getId(),usuario);
        // unlock semaforo
        sem_usuarios_registrados.release();
        
        // Guardamos la base de datos de forma permanente
        // Usamos los semaforos de RW sin que nos importe el orden de entrada
        // lock lectura semaforo
        try {
            mutex_usuarios_registrados.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numero_lectores_registro++;
        if (numero_lectores_registro == 1) {
            try {
                sem_usuarios_registrados.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mutex_usuarios_registrados.release();
        // Actualizamos el registro permanente
        try {
            saveUsuarios();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // unlock semaforo
        try {
            mutex_usuarios_registrados.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numero_lectores_registro--;
        if (numero_lectores_registro == 0) {
            sem_usuarios_registrados.release();
        }
        mutex_usuarios_registrados.release();
    }
    
    public void updateUsuario(Usuario usuario, String id_fichero) {
        // Guardamos la informacion del nuevo fichero que tiene el usuario
        // lock escritura monitorUsuarios
        monitor_ficheros.beginWrite();
        if (ficheros_disponibles.get(id_fichero) == null)
            ficheros_disponibles.put(id_fichero,new ArrayList<String>());
        if (!ficheros_disponibles.get(id_fichero).contains(usuario.getId()))
            ficheros_disponibles.get(id_fichero).add(usuario.getId());
        // unlock monitorUsuarios
        monitor_ficheros.endWrite();
        
        // lock escritura semaforo
        try {
            sem_usuarios_registrados.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Sobreescribimos el usuario registrado con la informacion que tenga ahora
        usuarios_registrados.put(usuario.getId(),usuario);
        // unlock semaforo
        sem_usuarios_registrados.release();
    }
    
    public void removeUsuario(String id_usuario) {
        // Borramos el usuario de usuarios conectados
        // lock escritura monitorUsuarios
        monitor_usuarios.beginWrite();
        usuarios_conectados.remove(id_usuario);
        // unlock monitorUsuarios
        monitor_usuarios.endWrite();
        
        // Borramos los ficheros del usuario de la lista de disponibles
        // lock escritura monitorFicheros
        monitor_ficheros.beginWrite();
        // Si la lista de propietarios queda vacia dejamos la clave
        for (String id_fichero : ficheros_disponibles.keySet()) {
            if (ficheros_disponibles.get(id_fichero).contains(id_usuario))
                ficheros_disponibles.get(id_fichero).remove(id_usuario);
        }
        // unlock monitorFicheros
        monitor_ficheros.endWrite();
        
        // Guardamos la base de datos de forma permanente
        // Usamos los semaforos de RW sin que nos importe el orden de entrada
        // lock lectura semaforo
        try {
            mutex_usuarios_registrados.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numero_lectores_registro++;
        if (numero_lectores_registro == 1) {
            try {
                sem_usuarios_registrados.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mutex_usuarios_registrados.release();
        // Actualizamos el registro permanente
        try {
            saveUsuarios();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // unlock semaforo
        try {
            mutex_usuarios_registrados.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numero_lectores_registro--;
        if (numero_lectores_registro == 0) {
            sem_usuarios_registrados.release();
        }
        mutex_usuarios_registrados.release();
    }
    
    public List<String> getIdUsuariosConectados() {
        // lock lectura monitorUsuarios
        monitor_usuarios.beginRead();
        List<String> id_usuarios_conectados = new ArrayList<String>();
        for (String id_usuario : usuarios_conectados.keySet()) {
            id_usuarios_conectados.add(id_usuario);
        }
        // unlock monitorUsuarios
        monitor_usuarios.endRead();
        return id_usuarios_conectados;
    }
    
    public List<String> getIdFicherosDisponibles() {
        // lock lectura monitorFicheros
        monitor_ficheros.beginRead();
        List<String> id_ficheros_disponibles = new ArrayList<String>();
        for (String id_fichero : ficheros_disponibles.keySet()) {
            // Si la lista de propietarios esta vacia no cogemos la clave
            if (!ficheros_disponibles.get(id_fichero).isEmpty())
                id_ficheros_disponibles.add(id_fichero);
        }
        // unlock monitorFicheros
        monitor_ficheros.endRead();
        return id_ficheros_disponibles;
    }
    
    public ObjectOutputStream getFlujoSalidaUsuario(String id_receptor, String id) {
        // lock lectura semaforo
        try {
            sem_usuarios_registrados.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Si usuarios_registrados contiene al id corresponde a un id_usuario
        // sino corresponde a un id_fichero
        String usuario_o_fichero = "usuario";
        if (!usuarios_registrados.containsKey(id)) {
            usuario_o_fichero = "fichero";
        }
        // unlock semaforo
        sem_usuarios_registrados.release();
        
        String id_fichero = "";
        String id_usuario = "";
        if (usuario_o_fichero.equals("fichero")) {
            id_fichero = id;
            // Tomo a un id_usuario que sea propietario del fichero id
            List<String> id_usuarios_conectados = getIdUsuariosConectados();
            
            // lock lectura MonitorFicheros
            monitor_ficheros.beginRead();
            List<String> id_propietarios_fichero = ficheros_disponibles.get(id_fichero);
            // unlock MonitorFicheros
            monitor_ficheros.endRead();
            
            for (String id_propietario_fichero : id_propietarios_fichero) {
                // Si el fichero corresponde al mismo usuario aparece como no existente
                if (id_usuarios_conectados.contains(id_propietario_fichero) && !id_propietario_fichero.equals(id_receptor))
                    id_usuario = id_propietario_fichero;
            }
            if (id_usuario.equals("")) return null;
        }
        else id_usuario = id;
        
        // lock lectura monitorUsuarios
        monitor_usuarios.beginRead();
        ObjectOutputStream flujo_salida_usuario = (ObjectOutputStream) usuarios_conectados.get(id_usuario).get(1);
        // unlock monitorUsuarios
        monitor_usuarios.endRead();
        
        return flujo_salida_usuario;
    }
    
    public int getPuerto(int numero_cliente) {
        lock_puerto.takeLock(numero_cliente);
        int puerto = (puerto_p2p+1)%10000;
        lock_puerto.releaseLock();
        return puerto;
    }
    
    
    public void loadUsuarios() throws IOException {
        // Leemos de users.txt los id de los usuarios registrados junto con su puerto, su ip y los ficheros que tenian la ultima vez que estuvieron conectados
        BufferedReader flujo_lectura = new BufferedReader(new InputStreamReader(new FileInputStream("test/servidor/users.txt")));
        
        String id_usuario = flujo_lectura.readLine(), ip_usuario;
        int puerto_usuario;
        while (id_usuario != null) {
            ip_usuario = flujo_lectura.readLine();
            puerto_usuario = Integer.parseInt(flujo_lectura.readLine());
            List<String> id_ficheros = Arrays.asList(flujo_lectura.readLine().split(" "));
            // Si usuarios_registrados ya contiene al id_usuario es que hay un usuario repetido en users.txt
            if (usuarios_registrados.containsKey(id_usuario)) {
                id_usuario = flujo_lectura.readLine();
                continue;
            }
            // Registro usuario
            Usuario usuario = new Usuario(id_usuario,ip_usuario,ip,puerto_usuario,id_ficheros);
            usuarios_registrados.put(id_usuario,usuario);
            
            id_usuario = flujo_lectura.readLine();
        }
    }
    
    public void saveUsuarios() throws IOException {
        String registro_usuarios = "";
        for (String id_usuario : usuarios_registrados.keySet()) {
            Usuario usuario = usuarios_registrados.get(id_usuario);
            registro_usuarios += (id_usuario + "\n");
            registro_usuarios += (usuario.getIp() + "\n");
            registro_usuarios += (usuario.getPuerto() + "\n");
            for(String id_fichero : usuario.getIdFicheros()){
                registro_usuarios += (id_fichero + " ");
            }
            registro_usuarios += "\n";
        }
        
        // Si el archivo no existe es creado
        File registro = new File("test/servidor/users.txt");
        if (!registro.exists()) {
            registro.createNewFile();
        }
        
        FileWriter escritor = new FileWriter(registro);
        BufferedWriter flujo_escritura = new BufferedWriter(escritor);
        flujo_escritura.write(registro_usuarios);
        flujo_escritura.close();
    }
    

    public void start() {
        // Leemos informacion de los usuarios registrados
        try {
            loadUsuarios();
            
            // Creamos el servidor para 100 clientes
            // InetAddress inetAddress = InetAddress.getLocalHost();
            ServerSocket canal_servidor = new ServerSocket(puerto_servidor,100,InetAddress.getByName(ip));
            int numero_cliente = 0;
            while (numero_cliente < MAX_CLIENTES) {
                // Esperamos a un cliente
                Socket canal = canal_servidor.accept();
                    
                // Creamos y ejecutamos un proceso canal de comunicacion
                OyenteCliente oyente_cliente = new OyenteCliente(this,canal,numero_cliente);
                oyente_cliente.start();
                
                numero_cliente++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
