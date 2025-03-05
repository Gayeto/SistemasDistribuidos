import java.io.*;
import java.net.*;

public class ClienteBoletos {
    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 6000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVIDOR, PUERTO);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("✅ Conectado al servidor.");

            // Leer mensajes iniciales del servidor
            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                System.out.println(respuesta);
                if (respuesta.contains("Escribe 'RESERVAR' para comprar un boleto.")) {
                    break; // Salir cuando termine el mensaje de bienvenida
                }
            }

            // Crear un lector de entrada de usuario (fuera del try para evitar cierre prematuro)
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            // Bucle principal
            while (true) {
                System.out.print(">> ");
                System.out.flush(); // Forzar impresión inmediata

                String mensajeUsuario = teclado.readLine(); // Leer entrada del usuario

                if (mensajeUsuario == null || mensajeUsuario.trim().isEmpty()) {
                    continue; // Ignorar entradas vacías
                }

                mensajeUsuario = mensajeUsuario.trim().toUpperCase(); // Convertir a mayúsculas
                salida.println(mensajeUsuario); // Enviar mensaje al servidor
                salida.flush(); // Asegurar que se envíe inmediatamente

                // Recibir respuesta del servidor
                respuesta = entrada.readLine();
                if (respuesta == null) {
                    System.out.println("El servidor cerró la conexión.");
                    break;
                }
                System.out.println("Respuesta del servidor: " + respuesta);

                if ("SALIR".equals(mensajeUsuario)) {
                    break; // Salir del bucle si el usuario escribe "SALIR"
                }
            }

            System.out.println("❌ Desconectado del servidor.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
