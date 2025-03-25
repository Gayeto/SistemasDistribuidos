import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            AjedrezInterfaz servidor = (AjedrezInterfaz) registry.lookup("AjedrezServidor");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese su número de jugador (1 o 2): ");
            int jugador = scanner.nextInt();
            scanner.nextLine();

            while (true) {
                System.out.println("\nTablero actual:");
                System.out.println(servidor.obtenerTablero());

                // Verificar si es el turno del jugador
                if (servidor.obtenerTurno() == jugador) {
                    System.out.print("Tu turno. Ingresa tu movimiento: ");
                    String movimiento = scanner.nextLine();

                    // Enviar movimiento al servidor
                    boolean exito = servidor.realizarMovimiento(movimiento, jugador);
                    if (!exito) {
                        System.out.println("Movimiento inválido. No es tu turno.");
                    }
                } else {
                    System.out.println("Esperando el turno del otro jugador...");
                }

                Thread.sleep(2000); // Pequeña pausa para evitar spam de peticiones
            }
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
