package application;

import application.Services.DroneService;
import application.websocket.ClientSocketHandler;
import application.websocket.DroneSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ObjectMapper objectMapper;
    private DroneService droneService;

    public WebSocketConfig(ObjectMapper objectMapper, DroneService droneService) {
        this.objectMapper = objectMapper;
        this.droneService = droneService;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(clientWebSocketHandler(), "/client")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");

        registry.addHandler(droneWebSocketHandler(), "/drone")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler clientWebSocketHandler() {
        return new ClientSocketHandler(objectMapper, droneService);
    }

    @Bean
    public WebSocketHandler droneWebSocketHandler() {
        return new DroneSocketHandler(objectMapper, droneService);
    }
}
