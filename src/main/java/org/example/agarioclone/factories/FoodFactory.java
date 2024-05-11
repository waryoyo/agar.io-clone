package org.example.agarioclone.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import javafx.scene.shape.Circle;
import org.example.agarioclone.EntityType;
import org.example.agarioclone.Utility;
import org.example.agarioclone.components.GooglyEyesComponent;

public class FoodFactory implements EntityFactory {
    @Spawns("food")
    public Entity newFood(SpawnData data) {
        var food =  FXGL.entityBuilder(data)
                .type(EntityType.FOOD)
                .at(Utility.getRandomPosition())
                .view(new Circle(10, 10,10, Utility.getRandomColor()))
                .bbox(BoundingShape.circle(10))
                .collidable()
                .build();

//        food.removeComponent(CollidableComponent.class);
        return food;

    }
    @Spawns("foodGoogly")
    public Entity newGooglyFood(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.FOOD)
                .at(Utility.getRandomPosition())
                .view(new Circle(10, 10,10, Utility.getRandomColor()))
                .bbox(BoundingShape.circle(10))
                .with(new GooglyEyesComponent())
                .collidable()
                .build();
    }
}
