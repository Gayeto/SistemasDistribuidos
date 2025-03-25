import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class Servidor extends UnicastRemoteObject implements AjedrezInterfaz {
    private String tablero;
    private int turno;
    private final List<String> movimientos;

    protected Servidor() throws RemoteException {
        super();
        this.tablero = "Estado inicial del tablero";
        this.turno = 1;
        this.movimientos = new ArrayList<>();
    }

    @Override
    public synchronized boolean realizarMovimiento(String movimiento, int jugador) throws RemoteException {
        if (jugador != turno) {
            System.out.println("Movimiento inválido: No es el turno del jugador " + jugador);
            return false; // No es el turno del jugador
        }

        System.out.println("Jugador " + jugador + " realizó el movimiento: " + movimiento);
        movimientos.add("Jugador " + jugador + ": " + movimiento);

        // Cambiar turno al otro jugador
        turno = (turno == 1) ? 2 : 1;

        return true;
    }

    @Override
    public synchronized String obtenerTablero() throws RemoteException {
        return tablero + "\nMovimientos: " + movimientos.toString();
    }

    @Override
    public synchronized int obtenerTurno() throws RemoteException {
        return turno;
    }
}
