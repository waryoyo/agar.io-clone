package org.example.agarioclone.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.example.agarioclone.EntityType;
import org.example.agarioclone.Utility;
import org.example.agarioclone.components.CameraComponent;
import org.example.agarioclone.components.GooglyEyesComponent;
import org.example.agarioclone.components.PlayerComponent;


public class PlayerFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        Color color = Utility.getRandomColor();
        Entity player = FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .at(Utility.getRandomPosition())
                .viewWithBBox(new Circle(50,50,50, color))
                .with(new PlayerComponent(100, color))
                .with(new GooglyEyesComponent())
                .with(new CameraComponent())
                .collidable()
                .build();

        // Adjust the camera zoom for the first time
        player.getComponent(CameraComponent.class).updateCamera(player, true);
        return player;
    }

}
