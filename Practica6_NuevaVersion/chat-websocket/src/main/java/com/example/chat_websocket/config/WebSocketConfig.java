package com.example.chat_websocket.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint para que los clientes se conecten
        registry.addEndpoint("/ws-chat")  // Usa "/ws-chat" en el frontend
                .setAllowedOriginPatterns("*")  // Permite conexiones desde cualquier origen (CORS)
                .withSockJS();  // Soporte para navegadores antiguos
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefijo para enviar mensajes desde el cliente al servidor
        registry.setApplicationDestinationPrefixes("/app");

        // Prefijo para suscribirse a mensajes del servidor
        registry.enableSimpleBroker("/topic");
    }
}