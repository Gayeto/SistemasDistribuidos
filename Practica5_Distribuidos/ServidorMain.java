import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorMain {
    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("AjedrezServidor", servidor);

            System.out.println("Servidor de Ajedrez en ejecución...");
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
