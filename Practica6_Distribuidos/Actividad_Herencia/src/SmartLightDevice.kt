class SmartLightDevice(nombreDisp: String, categoriaDisp: String) : SmartDevice1(nombre = nombreDisp, categoria = categoriaDisp) {

    override val tipodispositivo = "Smart Light"

    var nivelBrillo: Int = 100
        set(value) {
            if (value in 0..250) {
                field = value
            }
        }

    fun incrementarBrillo() {
        if (nivelBrillo < 250) {
            nivelBrillo += 10
            println("El brillo se incrementó a: $nivelBrillo.")
        } else {
            println("El brillo ya está en el máximo.")
        }
    }

    fun decrementarBrillo() {
        if (nivelBrillo > 0) {
            nivelBrillo -= 10
            println("El brillo se redujo a: $nivelBrillo.")
        } else {
            println("El brillo ya está en el mínimo.")
        }
    }

    override fun encender() {
        super.encender()
        println("$nombre está encendido con un brillo de $nivelBrillo.")
    }

    override fun apagar() {
        super.apagar()
        println("$nombre se ha apagado.")
    }
}
