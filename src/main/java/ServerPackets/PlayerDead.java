package ServerPackets;

import java.io.Serializable;

public class PlayerDead implements Serializable {
    public String entityId;
    public PlayerDead() {}
    public PlayerDead(String id) {
        entityId = id;
    }
}
