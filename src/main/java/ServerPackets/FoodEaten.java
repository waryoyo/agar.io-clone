package ServerPackets;

import java.io.Serializable;

public class FoodEaten implements Serializable {
    public String entityId;
    public FoodEaten() {}
    public FoodEaten(String id) {
        entityId = id;
    }
}
