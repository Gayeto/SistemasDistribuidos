from flask import Flask, jsonify, request

app = Flask(__name__)

# Simulación de base de datos
pacientes = [
    {"id": 1, "nombre": "Carlos Méndez", "edad": 30},
    {"id": 2, "nombre": "María Gómez", "edad": 25}
]

# Obtener todos los pacientes
@app.route('/pacientes', methods=['GET'])
def obtener_pacientes():
    return jsonify(pacientes)

# Obtener un paciente por ID
@app.route('/pacientes/<int:id>', methods=['GET'])
def obtener_paciente(id):
    paciente = next((p for p in pacientes if p["id"] == id), None)
    return jsonify(paciente) if paciente else ('Paciente no encontrado', 404)

# Crear un nuevo paciente
@app.route('/pacientes', methods=['POST'])
def agregar_paciente():
    nuevo = request.get_json()
    nuevo['id'] = pacientes[-1]['id'] + 1 if pacientes else 1
    pacientes.append(nuevo)
    return jsonify(nuevo), 201

if __name__ == '__main__':
    app.run(debug=True, port=5002)
