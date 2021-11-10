package src.Interfaz;

public class GUI {
    // Reset
    public static final String RESET = "\033[0m";
    
    // Format
    public static final String BOLD ="\033[1m";
    public static final String DIM = "\033[2m";
    public static final String ITALIC = "\033[3m";
    public static final String UNDERLINED = "\033[4m";
    public static final String BLINK = "\033[5m";
    public static final String RBLINK = "\033[25m";
    
    // Color
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    
    public static void print(String mensaje) {
        System.out.print(BOLD + mensaje + RESET);
    }
    
    public static void println(String mensaje) {
        System.out.println(BOLD + mensaje + RESET);
    }
    
    public static void println() {
        System.out.println();
    }
    
    public static void printTitulo(String titulo) {
        System.out.println(BOLD + UNDERLINED + titulo + RESET);
    }
    
    public static void printOpcion(int num_opcion, String opcion) {
        System.out.println(BOLD + num_opcion + " " + ITALIC + opcion + RESET);
    }
    
    public static void printNotificacion(String tipo, String mensaje) {
        System.out.print(BOLD + DIM + tipo + ": " + RESET);
        System.out.println(ITALIC + DIM + mensaje + RESET);
    }
    
    public static void printNotificacion(String mensaje) {
        System.out.println(DIM + ITALIC + mensaje + RESET);
    }
    
    public static void printIdUsuarioConectado(String id_usuario) {
        System.out.println(BOLD + GREEN + BLINK + "• " + RBLINK + id_usuario + RESET);
    }
    
    public static void printIdFichero(String id_fichero) {
        System.out.println(id_fichero);
    }
    
    // Imprime errores
    public static void printError(String error) {
        System.out.println(BOLD + RED + "[ERROR]: " + error + RESET);
    }
    
    // Metodo en https://stackoverflow.com/questions/852665/command-line-progress-bar-in-java
    public static void actualiza_progreso(int currentValue, int maxValue) {
        int progressBarLength = 24; //
        int currentProgressBarIndex = (int) Math.ceil(((double) progressBarLength / maxValue) * currentValue);

        StringBuilder sb = new StringBuilder();
        sb.append("┃");
        int progressBarIndex = 0;
        while (progressBarIndex < progressBarLength) {
            sb.append(currentProgressBarIndex <= progressBarIndex ? " " : "▉");
            progressBarIndex++;
        }
        sb.append("┃");
        
        System.out.print(DIM);
        System.out.print("\r" + sb);
    }
}
