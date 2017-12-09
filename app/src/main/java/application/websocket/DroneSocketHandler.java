package application.websocket;

import application.Services.DroneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class DroneSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private DroneService droneService;


    public DroneSocketHandler(ObjectMapper objectMapper, DroneService droneService) {
        this.objectMapper = objectMapper;
        this.droneService = droneService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        droneService.addDrone(webSocketSession);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        droneService.removeDrone(webSocketSession);
    }
}
