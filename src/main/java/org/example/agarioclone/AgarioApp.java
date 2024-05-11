package org.example.agarioclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.agarioclone.components.GooglyEyesComponent;
import org.example.agarioclone.components.PlayerComponent;
import org.example.agarioclone.factories.FoodFactory;
import org.example.agarioclone.factories.PlayerFactory;

import java.util.ArrayList;
import java.util.Map;

public class AgarioApp extends GameApplication {

    Entity player;
    public static final int WINDOW_WIDTH = 2000;
    public static final int WINDOW_HEIGHT = 2000;
    public static final int MAP_WIDTH = 2500;
    public static final int MAP_HEIGHT = 2500;
    Input input;
    ArrayList<Entity> food;
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
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("speed", 0.0);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        FXGL.getGameWorld().addEntityFactory(new FoodFactory());

        FXGL.getGameWorld().spawn("player");

        food = new ArrayList<>();
        FXGL.getGameTimer().runAtInterval(() -> {
            spawnFood(10);
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

                }

            }
        });
    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50);
        textPixels.setTranslateY(100);
        textPixels.setFont(new Font(26));

        FXGL.getGameScene().addUINode(textPixels);
        textPixels.textProperty().bind(FXGL.getWorldProperties().doubleProperty("speed").asString());

    }

    void spawnFood(int n) {
        for (int i = 0; i < n; ++i) {
            FXGL.getGameWorld().spawn("food");
        }
    }

}