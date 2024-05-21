package org.example.agarioclone.ServerPackets.AgarioEvents;

import java.io.Serializable;

public class DeadPlayerEvent implements Serializable {
    public String entityId;
    public DeadPlayerEvent() {}
    public DeadPlayerEvent(String id) {
        entityId = id;
    }
}
