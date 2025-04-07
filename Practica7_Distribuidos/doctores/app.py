from flask import Flask, jsonify, request

app = Flask(__name__)

doctores = [
    {"id": 1, "nombre": "Dr. Juan Pérez", "especialidad": "Cardiología"},
    {"id": 2, "nombre": "Dra. Ana López", "especialidad": "Dermatología"}
]

@app.route('/doctores', methods=['GET'])
def obtener_doctores():
    return jsonify(doctores)

@app.route('/doctores/<int:id>', methods=['GET'])
def obtener_doctor(id):
    doctor = next((d for d in doctores if d["id"] == id), None)
    return jsonify(doctor) if doctor else ("No encontrado", 404)

@app.route('/doctores', methods=['POST'])
def agregar_doctor():
    nuevo = request.json
    nuevo["id"] = len(doctores) + 1
    doctores.append(nuevo)
    return jsonify(nuevo), 201

if __name__ == '__main__':
    app.run(port=5001, debug=True)
