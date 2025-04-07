from flask import Flask, jsonify, request
import requests

app = Flask(__name__)

# Base URLs de otros servicios (ajusta si usas puertos distintos)
DOCTORES_SERVICE = "http://127.0.0.1:5001"
PACIENTES_SERVICE = "http://127.0.0.1:5002"

# Base de datos simulada en memoria
citas = []
id_counter = 1

@app.route('/citas', methods=['GET'])
def obtener_citas():
    return jsonify(citas)

@app.route('/citas/<int:id>', methods=['GET'])
def obtener_cita_por_id(id):
    for cita in citas:
        if cita['id'] == id:
            return jsonify(cita)
    return "Cita no encontrada", 404

@app.route('/citas', methods=['POST'])
def crear_cita():
    global id_counter
    data = request.get_json()
    paciente_id = data.get('paciente_id')
    doctor_id = data.get('doctor_id')

    # Obtener info del paciente desde el microservicio de pacientes
    paciente_response = requests.get(f"{PACIENTES_SERVICE}/pacientes/{paciente_id}")
    if paciente_response.status_code != 200:
        return "Paciente no encontrado", 404

    # Obtener info del doctor desde el microservicio de doctores
    doctor_response = requests.get(f"{DOCTORES_SERVICE}/doctores/{doctor_id}")
    if doctor_response.status_code != 200:
        return "Doctor no encontrado", 404

    nueva_cita = {
        'id': id_counter,
        'paciente': paciente_response.json(),
        'doctor': doctor_response.json()
    }
    citas.append(nueva_cita)
    id_counter += 1
    return jsonify(nueva_cita), 201

if __name__ == '__main__':
    app.run(port=5003, debug=True)
