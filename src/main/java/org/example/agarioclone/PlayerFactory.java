package org.example.agarioclone;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import javafx.scene.shape.Circle;

public class PlayerFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .at(Utility.getRandomPosition())
                .viewWithBBox(new Circle(50,50,50, Utility.getRandomColor()))
                .with(new PlayerComponent(50))
                .with(new GooglyEyesComponent())
                .collidable()
                .build();
    }
}
