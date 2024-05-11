package org.example.agarioclone.factories;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.example.agarioclone.EntityType;
import org.example.agarioclone.Utility;
import org.example.agarioclone.components.CameraComponent;
import org.example.agarioclone.components.GooglyEyesComponent;
import org.example.agarioclone.components.PlayerComponent;

import java.awt.*;

import static com.almasb.fxgl.core.math.FXGLMath.random;

public class PlayerFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        var emitter = ParticleEmitters.newExplosionEmitter(300);
        emitter.setColor(Color.RED);
        emitter.setEmissionRate(0.5);

        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .at(Utility.getRandomPosition())
                .viewWithBBox(new Circle(50,50,50, Utility.getRandomColor()))
                .with(new PlayerComponent(100))
                .with(new GooglyEyesComponent())
                .with(new CameraComponent())
                .with(new ParticleComponent(emitter))
                .collidable()
                .build();
    }
}
