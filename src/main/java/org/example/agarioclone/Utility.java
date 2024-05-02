package org.example.agarioclone;
import javafx.scene.paint.Color;

import java.util.Random;

public class Utility {
    static Color getRandomColor() {
        Random rng = new Random();
        int randNumber = Math.abs(rng.nextInt());;
        int r = randNumber % 256;
        int g = (randNumber >> 8) % 256;
        int b = (randNumber >> 16) % 256;
        return Color.rgb(r, g, b);
    }
}
