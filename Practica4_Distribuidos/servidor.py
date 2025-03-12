import socket
import threading
import queue

# Configuración de servidores conectados
servidores = [("localhost", 6001), ("localhost", 6002)]
reloj_lamport = 0
cola_solicitudes = queue.PriorityQueue()

def incrementar_reloj():
    """Incrementa y devuelve el reloj lógico."""
    global reloj_lamport
    reloj_lamport += 1
    return reloj_lamport

def sincronizar_reloj(reloj_recibido):
    """Sincroniza el reloj de Lamport."""
    global reloj_lamport
    reloj_lamport = max(reloj_lamport, reloj_recibido) + 1

def manejar_cliente(cliente_socket, direccion):
    """Maneja las conexiones de los clientes."""
    global reloj_lamport
    print(f"Cliente conectado desde {direccion}")

    while True:
        try:
            mensaje = cliente_socket.recv(1024).decode()
            if not mensaje:
                break

            # Extraer información del mensaje
            partes = mensaje.split(":")
            tipo_mensaje = partes[0]
            reloj_recibido = int(partes[1])

            # Sincronizar reloj lógico
            sincronizar_reloj(reloj_recibido)

            if tipo_mensaje == "RESERVA":
                # Agregar a la cola de solicitudes
                cola_solicitudes.put((reloj_lamport, direccion))
                print(f"Solicitud de reserva añadida con reloj {reloj_lamport}")

                # Responder confirmación
                respuesta = f"Reserva confirmada en servidor. Reloj: {reloj_lamport}"
                cliente_socket.send(respuesta.encode())

        except Exception as e:
            print(f"Error con {direccion}: {e}")
            break

    cliente_socket.close()

def iniciar_servidor(puerto):
    """Inicia el servidor y acepta conexiones."""
    servidor = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    servidor.bind(("localhost", puerto))
    servidor.listen(5)
    print(f"Servidor corriendo en el puerto {puerto}")

    while True:
        cliente_socket, direccion = servidor.accept()
        hilo_cliente = threading.Thread(target=manejar_cliente, args=(cliente_socket, direccion))
        hilo_cliente.start()

if __name__ == "__main__":
    puerto_servidor = int(input("Introduce el puerto del servidor: "))  
    iniciar_servidor(puerto_servidor)

