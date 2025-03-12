import socket
import random

reloj_lamport = 0
servidores = [("localhost", 6001), ("localhost", 6002)]  

def incrementar_reloj():
    """Incrementa y devuelve el reloj lógico."""
    global reloj_lamport
    reloj_lamport += 1
    return reloj_lamport

def cliente():
    global reloj_lamport
    while True:
        entrada = input("Escribe 'RESERVAR' para una cita o 'SALIR' para salir: ").upper()

        if entrada == "SALIR":
            print("Saliendo del sistema...")
            break

        if entrada == "RESERVAR":
            # Elegir un servidor aleatorio
            servidor_host, puerto_servidor = random.choice(servidores)

            try:
                cliente_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                cliente_socket.connect((servidor_host, puerto_servidor))
                print(f"Conectado a {servidor_host}:{puerto_servidor}")

                reloj_actual = incrementar_reloj()
                mensaje = f"RESERVA:{reloj_actual}"
                cliente_socket.send(mensaje.encode())

                respuesta = cliente_socket.recv(1024).decode()
                print(f"Respuesta del servidor: {respuesta}")

                cliente_socket.close()
            except Exception as e:
                print(f"Error al conectar con {servidor_host}:{puerto_servidor} - {e}")

if __name__ == "__main__":
    cliente()
