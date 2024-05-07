package org.example.agarioclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.shape.Circle;
import java.util.ArrayList;


import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class AgarioApp extends GameApplication {

    Entity player;
    final static public int WINDOW_WIDTH = 2000;
    final static public int WINDOW_HEIGHT = 2000;
    final static public int MAP_WIDTH = 5000;
    final static public int MAP_HEIGHT = 5000;
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
        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        FXGL.getGameWorld().addEntityFactory(new FoodFactory());

        FXGL.getGameWorld().spawn("player");

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
    void spawnFood(int n) {
        for (int i = 0; i < n; ++i) {
            FXGL.getGameWorld().spawn("food");
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if (frameCount == 0) {
            spawnFood(10);
        }
        frameCount++;
        frameCount %= 40;
    }
}