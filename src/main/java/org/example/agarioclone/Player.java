package org.example.agarioclone;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;


public class Player {
    private float radius;
    private Color color;
    private double x, y;
    public Player() {
        color = Utility.getRandomColor();
        radius = 10.0f;
    }
    public Player(Color color) {
        this.color = color;
    }
    public Player(double x, double y, Color color) {
        this.color = color;
        this.x = x;
        this.y = y;
    }
    public Player(double x, double y, float radius, Color color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

}
