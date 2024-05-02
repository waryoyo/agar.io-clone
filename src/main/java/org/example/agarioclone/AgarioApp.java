package org.example.agarioclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import  java.awt.*;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AgarioApp extends GameApplication {

    Entity player;
    final static public int WINDOW_WIDTH = 2000;
    final static public int WINDOW_HEIGHT = 2000;
    static public float MAX_PLAYER_SPEED = 50;
    Input input;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setFullScreenAllowed(true);
        // gameSettings.setFullScreenFromStart(true);

        // TODO: make it full screen dynamically
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // int width = (int)screenSize.getWidth();
        // int height = (int)screenSize.getHeight();

        gameSettings.setWidth(WINDOW_WIDTH);
        gameSettings.setHeight(WINDOW_HEIGHT);

    }

    @Override
    protected void initGame() {
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 1000)
                .viewWithBBox(new Circle(50, Utility.getRandomColor()))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Override
    protected void initInput() {
        super.initInput();
        input = FXGL.getInput();

    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);

        Point2D mouse = input.getMousePositionWorld();
        Point2D playerPosition = player.getPosition();
        Vec2 motion = new Vec2(playerPosition.subtract(mouse));
        if (motion.getLengthAndNormalize() > 20) {
            float speed = Math.min(motion.getLengthAndNormalize() * 100, MAX_PLAYER_SPEED);
            player.translateTowards(mouse, speed * tpf * 5);
        }

    }
}