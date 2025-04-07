fun main() {
    val camara = SmartCamDevice(nombreDisp = "Cámara de Seguridad", categoriaDisp = "Seguridad")

    camara.encender()
    camara.resolucion = 50
    camara.activarVisionNocturna()

    println("La resolución actual de la cámara es: ${camara.resolucion} MP")
    println("¿Visión nocturna activada? ${camara.modoVisionNocturna}")

    camara.desactivarVisionNocturna()
    camara.apagar()
}
