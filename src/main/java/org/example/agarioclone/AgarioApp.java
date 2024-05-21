package org.example.agarioclone;

import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionDetectionStrategy;
import com.almasb.fxgl.physics.CollisionHandler;

import org.example.agarioclone.ServerPackets.AgarioEvents.EatenFoodEvent;
import org.example.agarioclone.ServerPackets.AgarioEvents.DeadPlayerEvent;
import org.example.agarioclone.ServerPackets.AgarioPackets.PlayerStatusPacket;
import org.example.agarioclone.ServerPackets.AgarioRequests.FoodRequest;

import static org.example.agarioclone.Utility.calculateZoom;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.example.agarioclone.ServerPackets.AgarioPackets.FoodPacket;
import org.example.agarioclone.components.CameraComponent;
import org.example.agarioclone.components.GooglyEyesComponent;
import org.example.agarioclone.components.IDCustomComponent;
import org.example.agarioclone.components.PlayerComponent;
import org.example.agarioclone.factories.FoodFactory;
import org.example.agarioclone.factories.PlayerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.*;



import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class AgarioApp extends GameApplication {

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 1000;
    public static final int MAP_WIDTH = 5000;
    public static final int MAP_HEIGHT = 5000;
    public static Entity topBorder, bottomBorder, leftBorder, rightBorder;
    Text textPixels, textPixels2;
    Input input;

    private Client client;

     static class EntityManagement {
        static public Entity player;
        static HashMap<String, Entity> enemies = new HashMap<>();
        static HashMap<String, Entity> food = new HashMap<>();
        static private void updatePlayer(PlayerStatusPacket playerStatusPacket) {
            String id = playerStatusPacket.entityId;
            PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
            if (playerComponent.getId().equals(id)) {
                //updates for me
                return;
            }
            double x = playerStatusPacket.x;
            double y = playerStatusPacket.y;
            double radius = playerStatusPacket.radius;
            double colorR = playerStatusPacket.colorR;
            double colorG = playerStatusPacket.colorG;
            double colorB = playerStatusPacket.colorB;
            Color color = Color.color(colorR, colorG, colorB);
            if (!enemies.containsKey(id)) {
                Entity enemy = FXGL.entityBuilder()
                        .type(EntityType.ENEMY)
                        .at(x, y)
                        .viewWithBBox(new Circle(radius, radius, radius, color))
                        .with(new PlayerComponent(100,  id, color))
                        .collidable()
                        .build();
                enemies.put(id, enemy);

                FXGL.getGameWorld().addEntity(enemy);
            }
            else {
                Entity enemy = enemies.get(id);
                enemy.setPosition(x, y);
                enemy.getComponent(PlayerComponent.class).setRadius(radius);
            }
        }

        static private void addFoodBit(FoodPacket foodEntity) {
            String id = foodEntity.entityId;
            if (food.containsKey(id))
                return;
            double x = foodEntity.x;
            double y = foodEntity.y;
            double colorR = foodEntity.colorR;
            double colorG = foodEntity.colorG;
            double colorB = foodEntity.colorB;
            Color color = Color.color(colorR, colorG, colorB);
            Entity foodBit =  FXGL.entityBuilder()
                    .type(EntityType.FOOD)
                    .at(x, y)
                    .view(new Circle(10, 10,10, color))
                    .with(new IDCustomComponent(id))
                    .bbox(BoundingShape.circle(10))
                    .collidable()
                    .build();
            food.put(id, foodBit);
            FXGL.getGameWorld().addEntity(foodBit);
        }
        static private void removeFood(String id) {
            try {
                food.get(id).removeFromWorld();
                food.remove(id);
            } catch (Exception e) {
                // already eaten
                System.err.println("Entity [" + id + "] already eaten...");
            }
        }

        static private void removeDeadPlayer(DeadPlayerEvent deadPlayerEvent) {
            String id = deadPlayerEvent.entityId;
            PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
            if (playerComponent.getId().equals(id)) {
                System.out.println("You are dead :(");
                System.exit(0);
            }
            try {
                enemies.get(id).removeFromWorld();
                enemies.remove(id);
                // already dead
            } catch (Exception e) {
                System.err.println("Entity [" + id + "] already removed...");
            }
        }
    }


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
        vars.put("zoom", Utility.calculateZoom(50));
    }



    @Override
    protected void initGame() {
        topBorder = entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(0, 0, MAP_WIDTH, 0))
                .collidable()
                .buildAndAttach();
        bottomBorder = entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(0, MAP_HEIGHT, MAP_WIDTH, MAP_HEIGHT))
                .collidable()
                .buildAndAttach();
        leftBorder = entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(0, 0, 0, MAP_HEIGHT))
                .collidable()
                .buildAndAttach();
        rightBorder = entityBuilder()
                .type(EntityType.BARRIER)
                .viewWithBBox(new Line(MAP_WIDTH, 0, MAP_WIDTH, MAP_HEIGHT))
                .collidable()
                .buildAndAttach();
        getGameWorld().addEntityFactory(new PlayerFactory());
        getGameWorld().addEntityFactory(new FoodFactory());

        EntityManagement.player = spawn("player");
        getGameWorld().addEntity(EntityManagement.player);

        try {
            client = new Client(1000000, 1000000);
            client.start();
            client.getKryo().register(PlayerStatusPacket.class);
            client.getKryo().register(FoodPacket.class);
            client.getKryo().register(EatenFoodEvent.class);
            client.getKryo().register(FoodRequest.class);
            client.getKryo().register(DeadPlayerEvent.class);
            client.addListener(new Listener() {
                @Override
                public void connected(Connection connection) {
                    System.out.println("Connected to server");
                    client.sendTCP(new FoodRequest());
                    System.out.println("Sent request!");
                }


                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof PlayerStatusPacket playerStatusPacket) {
                        // Update local game state with the received position
                        getExecutor().startAsyncFX(() -> EntityManagement.updatePlayer(playerStatusPacket));
                    }
                    if (object instanceof EatenFoodEvent eatenFoodEvent) {
                        // Update local game state with the received position
                        getExecutor().startAsyncFX(() -> EntityManagement.removeFood(eatenFoodEvent.entityId));
                    }
                    if (object instanceof FoodPacket foodPacket) {
                        // Update local game state with the received position
                        getExecutor().startAsyncFX(() -> EntityManagement.addFoodBit(foodPacket));
                    }
                    if (object instanceof DeadPlayerEvent deadPlayerEvent) {
                        // Update local game state with the received position
                        getExecutor().startAsyncFX(() -> EntityManagement.removeDeadPlayer(deadPlayerEvent));
                    }
                }
            });
            client.connect(5000, "localhost", 54555, 54777);
        } catch (IOException e){
            System.err.println("Connection Timeout... Try again.");
            System.exit(1);
        }

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
                client.sendTCP(new EatenFoodEvent(food.getComponent(IDCustomComponent.class).id));
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollision(Entity player, Entity enemy) {

                var playerView = player.getViewComponent().getChild(0, Circle.class);
                var enemyView = enemy.getViewComponent().getChild(0, Circle.class);

                // if not larger enough then return, only consider eating if it is at least 20% larger
                if (playerView.getRadius() < enemyView.getRadius() + 0.2 * enemyView.getRadius()) return;

                var playerCenterPoint = player.getPosition().add(playerView.getRadius(), playerView.getRadius());
                var enemyCenterPoint = enemy.getPosition().add(enemyView.getRadius(), enemyView.getRadius());

                // make sure the player covers most of the player
                if(playerCenterPoint.distance(enemyCenterPoint) < playerView.getRadius() - playerView.getRadius() * 0.1){

                    player.getComponent(PlayerComponent.class).grow(enemyView.getRadius());
                    player.getComponent(GooglyEyesComponent.class).grow();
                    client.sendTCP(new DeadPlayerEvent(enemy.getComponent(PlayerComponent.class).getId()));

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

    @Override
    protected void onUpdate(double tpf) {
        textPixels.textProperty().set(1 / tpf + "\n" + FXGL.getGameWorld().getEntitiesByType(EntityType.FOOD).size());
        PlayerComponent playerComponent = EntityManagement.player.getComponent(PlayerComponent.class);
        double radius = EntityManagement.player.getViewComponent().getChild(0, Circle.class).getRadius();
        PlayerStatusPacket playerStatusPacket =new PlayerStatusPacket(
                playerComponent.getId(),
                EntityManagement.player.getX(),
                EntityManagement.player.getY(),
                radius,
                playerComponent.getColor().getRed(),
                playerComponent.getColor().getGreen(),
                playerComponent.getColor().getBlue());
        client.sendTCP(playerStatusPacket);
    }
}