package org.example.agarioclone.ServerPackets.AgarioEvents;

import java.io.Serializable;

public class EatenFoodEvent implements Serializable {
    public String entityId;
    public EatenFoodEvent() {}
    public EatenFoodEvent(String id) {
        entityId = id;
    }
}
