open class SmartDevice1(val nombre: String, val categoria: String) {

    var edoDispositivo: String = "encendido"
    open val tipodispositivo: String = "Dispositivo Inteligente"

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

    open fun encender() {
        println("El dispositivo $nombre está encendido")
    }

    open fun apagar() {
        println("El dispositivo $nombre está apagado")
    }
}
