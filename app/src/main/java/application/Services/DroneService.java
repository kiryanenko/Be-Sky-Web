package application.Services;

import application.views.FlightVector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class DroneService {
    private List<WebSocketSession> drones = new LinkedList<>();
    private final ObjectMapper objectMapper;


    public DroneService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public void addDrone(WebSocketSession session) {
        drones.add(session);
    }


    public void removeDrone(WebSocketSession session) {
        drones.remove(session);
    }


    public void broadcast(FlightVector vector) {
        final Iterator<WebSocketSession> it = drones.iterator();
        while (it.hasNext()) {
            final WebSocketSession ws = it.next();
            try {
                ws.sendMessage(new TextMessage(objectMapper.writeValueAsString(vector)));
            } catch (IOException e) {
                e.printStackTrace();
                it.remove();
            }
        }
    }
}
