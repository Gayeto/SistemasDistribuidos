class SmartDevice(val nombre: String, val categoria: String) {

    var edoDispositivo: String = "encendido" // Estado inicial del dispositivo

    constructor(nombre: String, categoria: String, codigoEdo: Int) : this(nombre, categoria) {
        edoDispositivo = when (codigoEdo) {
            0 -> "apagado"
            1 -> "encendido"
            else -> "desconocido"
        }
    }

    var volumen: Int = 2
        set(value) {
            if (value in 0..100) {
                field = value
            }
        }
        get() = field

    fun encender() {
        println("El smart device está encendido")
    }

    fun apagar() {
        println("El smart device está apagado.")
    }
}



