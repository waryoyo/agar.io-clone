package org.example.agarioclone;
import javafx.scene.paint.Color;

import java.util.Random;
import javafx.geometry.Point2D;

public final class Utility {

    private Utility(){};
    public static final Random rng = new Random();
    public static Color getRandomColor() {
        int randNumber = Math.abs(rng.nextInt());;
        int r = randNumber % 256;
        int g = (randNumber >> 8) % 256;
        int b = (randNumber >> 16) % 256;
        return Color.rgb(r, g, b);
    }

    public static Point2D getRandomPosition() {
        return new Point2D(rng.nextInt(AgarioApp.MAP_WIDTH), rng.nextInt(AgarioApp.MAP_HEIGHT));
    }
}
