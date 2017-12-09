package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightVector {
    @JsonProperty("trust")
    private Integer trust = 0;

    @JsonProperty("side")
    private Integer side = 0;

    @JsonProperty("rotation")
    private Integer rotation = 0;

    @JsonProperty("forward")
    private Integer forward = 0;


    public FlightVector() {
    }


    @JsonCreator
    public FlightVector(@JsonProperty("trust") Integer trust,
                        @JsonProperty("side") Integer side,
                        @JsonProperty("rotation") Integer rotation,
                        @JsonProperty("forward") Integer forward) {
        this.trust = trust == null ? 0 : trust;
        this.side = side == null ? 0 : side;
        this.rotation = rotation == null ? 0 : rotation;
        this.forward = forward == null ? 0 : forward;
    }


    public Integer getTrust() {
        return trust;
    }

    public Integer getSide() {
        return side;
    }

    public Integer getRotation() {
        return rotation;
    }

    public Integer getForward() {
        return forward;
    }
}
