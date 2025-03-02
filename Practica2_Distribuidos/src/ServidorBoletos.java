import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorBoletos {
    private static final int PUERTO = 6000;
    private static final int BOLETOS_TOTALES = 5;
    private static AtomicInteger boletosDisponibles = new AtomicInteger(BOLETOS_TOTALES);

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado en el puerto " + PUERTO);
            while (true) {
                Socket socketCliente = servidor.accept();
                System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress());
                new HiloCliente(socketCliente).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class HiloCliente extends Thread {
        private Socket socket;

        public HiloCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
            ) {
                salida.println("Bienvenido al sistema de reserva de boletos.");
                salida.println("Boletos disponibles: " + boletosDisponibles.get());

                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    System.out.println("Mensaje recibido: " + mensaje);

                    if (mensaje.equalsIgnoreCase("RESERVAR")) {
                        if (boletosDisponibles.get() > 0) {
                            int boletosRestantes = boletosDisponibles.decrementAndGet();
                            salida.println("¡Boleto reservado con éxito! Boletos restantes: " + boletosRestantes);
                        } else {
                            salida.println("No hay boletos disponibles.");
                        }
                    } else if (mensaje.equalsIgnoreCase("SALIR")) {
                        salida.println("Adiós.");
                        break;
                    } else {
                        salida.println("Comando no reconocido. Usa 'RESERVAR' o 'SALIR'.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    System.out.println("Cliente desconectado: " + socket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
