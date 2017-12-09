package application.websocket;

import application.Services.DroneService;
import application.views.FlightVector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;


public class ClientSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private DroneService droneService;

    public ClientSocketHandler(ObjectMapper objectMapper, DroneService droneService) {
        this.objectMapper = objectMapper;
        this.droneService = droneService;
    }


    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }

        try {
            final FlightVector vector = objectMapper.readValue(message.getPayload(), FlightVector.class);
            droneService.broadcast(vector);
        } catch (IOException ex) {
            try {
                webSocketSession.close(CloseStatus.BAD_DATA);
            } catch (IOException ignore) {
            }
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        droneService.broadcast(new FlightVector());
    }
}
