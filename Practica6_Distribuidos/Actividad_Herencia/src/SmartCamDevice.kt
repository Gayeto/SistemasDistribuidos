class SmartCamDevice(nombreDisp: String, categoriaDisp: String) : SmartDevice1(nombre = nombreDisp, categoria = categoriaDisp) {

    override val tipodispositivo = "Cámara Inteligente"

    var resolucion: Int = 10
        set(value) {
            if (value in 1..100) {
                field = value
            } else {
                println("Valor inválido. La resolución debe estar entre 1 y 100 MP.")
            }
        }

    var modoVisionNocturna: Boolean = false

    fun activarVisionNocturna() {
        modoVisionNocturna = true
        println("Visión nocturna activada.")
    }

    fun desactivarVisionNocturna() {
        modoVisionNocturna = false
        println("Visión nocturna desactivada.")
    }

    override fun encender() {
        super.encender()
        println("$nombre está encendida con una resolución de $resolucion MP.")
    }

    override fun apagar() {
        super.apagar()
        println("$nombre se ha apagado.")
    }
}
