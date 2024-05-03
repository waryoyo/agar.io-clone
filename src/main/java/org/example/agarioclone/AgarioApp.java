package org.example.agarioclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import  java.awt.*;

import com.almasb.fxgl.core.View;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AgarioApp extends GameApplication {

    Entity player;
    final static public int WINDOW_WIDTH = 2000;
    final static public int WINDOW_HEIGHT = 2000;
    static public float MAX_PLAYER_SPEED = 50;
    Input input;
    ArrayList<Entity> food;
    static int frameCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setFullScreenAllowed(true);
        gameSettings.setDeveloperMenuEnabled(true);
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
                .viewWithBBox(new Circle(50,50,50, Utility.getRandomColor()))
                .collidable()
                .buildAndAttach();
        food = new ArrayList<>();
    }

    @Override
    protected void initInput() {
        super.initInput();
        input = FXGL.getInput();

    }
    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FOOD) {
            @Override
            protected void onCollision(Entity player, Entity food) {

                food.removeFromWorld();

                Circle oldCircle = player.getViewComponent().getChild(0, Circle.class);
                int newRadius = (int) (oldCircle.getRadius() + 1);

                player.getViewComponent().clearChildren();
                player.getViewComponent().addChild(new Circle(newRadius, newRadius, newRadius, oldCircle.getFill()));

                player.getBoundingBoxComponent().clearHitBoxes();
                player.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.circle(newRadius)));


            }
        });
    }
    void spawnFood(int n) {
        for (int i = 0; i < n; ++i) {
            food.add(FXGL.entityBuilder()
                    .type(EntityType.FOOD)
                    .at(Utility.getRandomPosition())
                    .view(new Circle(10, 10,10, Utility.getRandomColor()))
                    .bbox(new HitBox(BoundingShape.circle(10)))
                    .collidable()
                    .buildAndAttach()
            );
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        Circle oldCircle = player.getViewComponent().getChild(0, Circle.class);
        Point2D mouse = input.getMousePositionWorld().subtract(oldCircle.getRadius(), oldCircle.getRadius());
        Point2D playerPosition = new Point2D(player.getX(), player.getY());
        Vec2 motion = new Vec2(playerPosition.subtract(mouse));
        if (motion.getLengthAndNormalize() > oldCircle.getRadius()/2) {
            float speed = Math.min(new Vec2(playerPosition.subtract(mouse)).getLengthAndNormalize() * 100, MAX_PLAYER_SPEED);
            player.translateTowards(mouse, speed * tpf * 5);
        }


        if (frameCount == 0) {
            spawnFood(5);
        }
        frameCount++;
        frameCount %= 60;
    }
}