package org.example.agarioclone;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static org.example.agarioclone.AgarioApp.WINDOW_HEIGHT;
import static org.example.agarioclone.AgarioApp.WINDOW_WIDTH;

public class PlayerComponent extends Component {
    public TransformComponent position;
    public Input input;
    public final int MAX_PLAYER_SPEED;
    public int size = 0;

    public PlayerComponent(int maxPlayerSpeed) {
        this.MAX_PLAYER_SPEED = maxPlayerSpeed;
    }

    public void onAdded() {
        super.onAdded();
        input = FXGL.getInput();
        getGameScene().getViewport().bindToEntity(entity, (int)(WINDOW_WIDTH / 2), (int)(WINDOW_HEIGHT / 2));
    }

    public void onUpdate(double tpf) {
        Circle oldCircle = entity.getViewComponent().getChild(0, Circle.class);
        Point2D mouse = input.getMousePositionWorld().subtract(oldCircle.getRadius(), oldCircle.getRadius());
        Point2D playerPosition = new Point2D(position.getX(), position.getY());
        Vec2 motion = new Vec2(playerPosition.subtract(mouse));

        if (motion.getLengthAndNormalize() > oldCircle.getRadius() / 2.0) {
            float speed = Math.min((new Vec2(playerPosition.subtract(mouse))).getLengthAndNormalize() * 100.0F, MAX_PLAYER_SPEED);
            position.translateTowards(mouse, speed * tpf * 5.0);
        }

    }

    public void grow() {
        Circle playerView = entity.getViewComponent().getChild(0, Circle.class);
        double newRadius = playerView.getRadius() + 1.0;

        playerView.setRadius(newRadius);
        playerView.setCenterX(newRadius);
        playerView.setCenterY(newRadius);

        entity.getBoundingBoxComponent().clearHitBoxes();
        entity.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.circle(newRadius)));
    }
}
