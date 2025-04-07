class SmartTvDevice(nombreDisp: String, categoriaDisp: String) : SmartDevice1(nombre = nombreDisp, categoria = categoriaDisp) {

    override val tipodispositivo = "Smart TV"

    var canal: Int = 1
        set(value) {
            if (value in 0..200) {
                field = value
            }
        }

    fun subirVolumen() {
        if (volumen < 100) {
            volumen++
            println("El volumen se incrementó a: $volumen.")
        } else {
            println("El volumen ya está al máximo.")
        }
    }

    fun siguienteCanal() {
        if (canal < 200) {
            canal++
            println("El canal se incrementó a: $canal.")
        } else {
            println("Ya estás en el canal máximo.")
        }
    }

    override fun encender() {
        super.encender()
        println("$nombre está encendido. El volumen es $volumen y el canal es $canal.")
    }

    override fun apagar() {
        super.apagar()
        println("$nombre se ha apagado.")
    }
}
