import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/message") // El cliente envía mensajes aquí ("/app/message")
    @SendTo("/topic/messages") // Todos los clientes suscritos a "/topic/messages" los recibirán
    public String processMessage(String message) {
        System.out.println("Mensaje recibido en el servidor: " + message);
        return "Usuario: " + message; // Esto es lo que recibirán los clientes
    }
}
