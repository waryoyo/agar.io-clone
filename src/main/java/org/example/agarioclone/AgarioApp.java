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

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class AgarioApp extends GameApplication {

    Entity player;
    final static public int WINDOW_WIDTH = 2000;
    final static public int WINDOW_HEIGHT = 2000;
    final static public int MAP_WIDTH = 5000;
    final static public int MAP_HEIGHT = 5000;
    static public float MAX_PLAYER_SPEED = 50;

    static private int playerScore = 10;
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
        int startRadius = 50;
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(Utility.getRandomPosition())
                .viewWithBBox(new Circle(startRadius,startRadius,startRadius, Utility.getRandomColor()))
                .collidable()
                .buildAndAttach();
        food = new ArrayList<>();
        getGameScene().getViewport().bindToEntity(player, (int)(WINDOW_WIDTH / 2) - startRadius, (int)(WINDOW_HEIGHT / 2) - startRadius);
        getGameScene().getViewport().setZoom(calculateZoom(startRadius));
    }

    @Override
    protected void initInput() {
        super.initInput();
        input = FXGL.getInput();

    }

    protected double calculateZoom(int radius) {
        return 2 * Math.max(0.01, (15 - Math.log10(radius) / Math.log10(2)) / 15);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FOOD) {
            @Override
            protected void onCollision(Entity player, Entity food) {
                playerScore++;

                var playerView = player.getViewComponent().getChild(0, Circle.class);
                var playerCenterPoint = player.getPosition().add(playerView.getRadius(), playerView.getRadius());

                var foodView = food.getViewComponent().getChild(0, Circle.class);
                var foodCenterPoint = food.getPosition().add(foodView.getRadius(), foodView.getRadius());

                // just make sure the collision is correct
                if(playerCenterPoint.distance(foodCenterPoint) < playerView.getRadius() + foodView.getRadius()){
                    food.removeFromWorld();

                    int newRadius = (int) (playerView.getRadius() + 1);

                    // change attributes instead of making a new object
                    playerView.setRadius(newRadius);
                    playerView.setCenterX(newRadius);
                    playerView.setCenterY(newRadius);


                    player.getBoundingBoxComponent().clearHitBoxes();
                    player.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.circle(newRadius)));
                    double zoom = calculateZoom(newRadius);
                    getGameScene().getViewport().setZoom(zoom);
                    getGameScene().getViewport().bindToEntity(player, (int)(WINDOW_WIDTH / (2 * zoom)) - newRadius, (int)(WINDOW_HEIGHT / (2 * zoom)) - newRadius);
                    System.err.println(getGameScene().getViewport().getZoom());
                }

            }
        });
    }
    void spawnFood(int n) {
        for (int i = 0; i < n; ++i) {
            food.add(FXGL.entityBuilder()
                    .type(EntityType.FOOD)
                    .at(Utility.getRandomPosition())
                    .view(new Circle(10, 10,10, Utility.getRandomColor()))
                    .bbox(BoundingShape.circle(10))
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
            spawnFood(100);
        }
        frameCount++;
        frameCount %= 40;
    }
}