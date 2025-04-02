package com.example.chat_websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Escucha mensajes enviados a "/app/message"
    @MessageMapping("/message")
    // Reenvía el mensaje a todos suscritos a "/topic/messages"
    @SendTo("/topic/messages")
    public String handleMessage(String message) {
        System.out.println("Mensaje recibido: " + message);
        return message;
    }
}