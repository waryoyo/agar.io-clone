package org.example.agarioclone;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.CollisionDetectionStrategy;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.agarioclone.components.CameraComponent;
import org.example.agarioclone.components.GooglyEyesComponent;
import org.example.agarioclone.components.PlayerComponent;
import org.example.agarioclone.factories.FoodFactory;
import org.example.agarioclone.factories.PlayerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static org.example.agarioclone.Utility.calculateZoom;

public class AgarioApp extends GameApplication {

    public static int WINDOW_WIDTH = 2000;
    public static int WINDOW_HEIGHT = 2000;
    public static final int MAP_WIDTH = 5000;
    public static final int MAP_HEIGHT = 5000;
    public static Entity topBorder, bottomBorder, leftBorder, rightBorder;
    Text textPixels, textPixels2;
    Input input;
    ArrayList<Entity> food;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setFullScreenAllowed(true);
        gameSettings.setDeveloperMenuEnabled(true);
        gameSettings.setFullScreenFromStart(true);
        gameSettings.setCollisionDetectionStrategy(CollisionDetectionStrategy.GRID_INDEXING);


        // TODO: make it full screen dynamically
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WINDOW_WIDTH = (int)screenSize.getWidth();
        WINDOW_HEIGHT = (int)screenSize.getHeight();

        gameSettings.setWidth(WINDOW_WIDTH);
        gameSettings.setHeight(WINDOW_HEIGHT);

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("speed", 0.0);
        vars.put("zoom", 1.0);
    }

    @Override
    protected void initGame() {
        topBorder = FXGL.entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(0, 0, MAP_WIDTH, 0))
                .collidable()
                .buildAndAttach();
        bottomBorder = FXGL.entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(0, MAP_HEIGHT, MAP_WIDTH, MAP_HEIGHT))
                .collidable()
                .buildAndAttach();
        leftBorder = FXGL.entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(0, 0, 0, MAP_HEIGHT))
                .collidable()
                .buildAndAttach();
        rightBorder = FXGL.entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(MAP_WIDTH, 0, MAP_WIDTH, MAP_HEIGHT))
                .collidable()
                .buildAndAttach();
        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        FXGL.getGameWorld().addEntityFactory(new FoodFactory());

        FXGL.getGameWorld().spawn("player");

        food = new ArrayList<>();
        FXGL.getGameTimer().runAtInterval(() -> {
            spawnFood(12);
        }, Duration.seconds(1));

    }

    @Override
    protected void initInput() {
        input = FXGL.getInput();
    }



    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FOOD) {
            @Override
            protected void onCollision(Entity player, Entity food) {


                var playerView = player.getViewComponent().getChild(0, Circle.class);
                var playerCenterPoint = player.getPosition().add(playerView.getRadius(), playerView.getRadius());

                var foodView = food.getViewComponent().getChild(0, Circle.class);
                var foodCenterPoint = food.getPosition().add(foodView.getRadius(), foodView.getRadius());

                // just make sure the collision is correct
                if(playerCenterPoint.distance(foodCenterPoint) < playerView.getRadius() + foodView.getRadius()){
                    food.removeFromWorld();

                    player.getComponent(PlayerComponent.class).grow();
                    player.getComponent(GooglyEyesComponent.class).grow();

                    FXGL.set("zoom", calculateZoom(playerView.getRadius()));
                    textPixels2.textProperty().set(String.valueOf(playerView.getRadius()));
                    player.getComponent(CameraComponent.class).updateCamera(player, true);
                }
            }
        });
    }

    @Override
    protected void initUI() {
        textPixels = new Text();
        textPixels.setTranslateX(50);
        textPixels.setTranslateY(100);
        textPixels.setFont(new Font(26));

        textPixels2 = new Text();
        textPixels2.setTranslateX(50);
        textPixels2.setTranslateY(200);
        textPixels2.setFont(new Font(26));

        FXGL.getGameScene().addUINode(textPixels);
        FXGL.getGameScene().addUINode(textPixels2);

//        textPixels.textProperty().bind(FXGL.getWorldProperties().doubleProperty("zoom").asString());


    }

    void spawnFood(int n) {
        for (int i = 0; i < n; ++i) {
            FXGL.getGameWorld().spawn("food");
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        textPixels.textProperty().set(1 / tpf + "\n" + FXGL.getGameWorld().getEntitiesByType(EntityType.FOOD).size());
    }
}