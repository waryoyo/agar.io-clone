package org.example.agarioclone.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.example.agarioclone.AgarioApp;

import static com.almasb.fxgl.core.math.FXGLMath.random;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;
import static org.example.agarioclone.AgarioApp.*;

public class PlayerComponent extends Component {
    public TransformComponent position;
    public Input input;
    public final int MAX_PLAYER_SPEED;
    public int size = 0;
    public ParticleEmitter emitter;
    public PlayerComponent(int maxPlayerSpeed) {
        this.MAX_PLAYER_SPEED = maxPlayerSpeed;
    }

    public void onAdded() {
        super.onAdded();
        input = FXGL.getInput();

    }

    public void onUpdate(double tpf) {

        Circle oldCircle = entity.getViewComponent().getChild(0, Circle.class);
        Point2D mouse = input.getMousePositionUI()
                .subtract(oldCircle.getRadius(), oldCircle.getRadius())
                .add(getGameScene().getViewport().getX(),getGameScene().getViewport().getY());

        Point2D playerPosition = new Point2D(position.getX(), position.getY());
        Vec2 motion = new Vec2(playerPosition.subtract(mouse));


        if (motion.getLengthAndNormalize() > oldCircle.getRadius() / 2.0) {
            double zoom = FXGL.getWorldProperties().getDouble("zoom");
            double speed = Math.max((new Vec2(playerPosition.subtract(mouse))).getLengthAndNormalize(), MAX_PLAYER_SPEED);
            FXGL.set("speed", speed * tpf * zoom);
            position.translateTowards(mouse, speed * tpf * zoom);
            if (position.getX() < 0) position.setX(0);
            if (position.getX() + 2 * oldCircle.getRadius() > AgarioApp.MAP_WIDTH) position.setX(AgarioApp.MAP_WIDTH - 2 * oldCircle.getRadius());
            if (position.getY() < 0) position.setY(0);
            if (position.getY() + 2 * oldCircle.getRadius() > AgarioApp.MAP_HEIGHT) position.setY(AgarioApp.MAP_HEIGHT - 2 * oldCircle.getRadius());
        }
    }

    public void grow() {
        Circle playerView = entity.getViewComponent().getChild(0, Circle.class);
        double newRadius = playerView.getRadius() + 0.4;

        playerView.setRadius(newRadius);
        playerView.setCenterX(newRadius);
        playerView.setCenterY(newRadius);

        entity.getBoundingBoxComponent().clearHitBoxes();
        entity.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.circle(newRadius)));
    }
}
