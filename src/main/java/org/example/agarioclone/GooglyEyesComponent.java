
package org.example.agarioclone;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.input.Input;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GooglyEyesComponent extends Component {
    public TransformComponent position;
    public Input input;
    public Circle leftEye;
    public Circle rightEye;
    public Circle leftInnerEye;
    public Circle rightInnerEye;
    public int size = 0;

    public GooglyEyesComponent() {
    }

    public void onAdded() {
        super.onAdded();
        this.input = FXGL.getInput();
        double size = entity.getViewComponent().getChild(0, Circle.class).getRadius();

        this.leftEye = new Circle(size - size * 0.4, size * 0.7, size * 0.2, Color.WHITE);
        this.rightEye = new Circle(size + size * 0.4, size * 0.7, size * 0.2, Color.WHITE);
        this.leftInnerEye = new Circle(size - size * 0.4, size * 0.7, size * 0.1, Color.BLACK);
        this.rightInnerEye = new Circle(size + size * 0.4, size * 0.7, size * 0.1, Color.BLACK);

        this.entity.getViewComponent().addChild(this.leftEye);
        this.entity.getViewComponent().addChild(this.leftInnerEye);
        this.entity.getViewComponent().addChild(this.rightEye);
        this.entity.getViewComponent().addChild(this.rightInnerEye);
    }

    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        Circle mainView = entity.getViewComponent().getChild(0, Circle.class);
        Point2D mouse = this.input.getMousePositionWorld().subtract(mainView.getRadius(), mainView.getRadius());
        Point2D playerPosition = new Point2D(this.entity.getX(), this.entity.getY());
        Vec2 direction = new Vec2(mouse.subtract(playerPosition));
        Vec2 normalizeDirection = direction.normalize();

        this.leftInnerEye.setCenterX(this.leftEye.getCenterX() + normalizeDirection.x * (leftEye.getRadius() - leftInnerEye.getRadius()));
        this.leftInnerEye.setCenterY(this.leftEye.getCenterY() + normalizeDirection.y * (leftEye.getRadius() - leftInnerEye.getRadius()));
        this.rightInnerEye.setCenterX(this.rightEye.getCenterX() + normalizeDirection.x * (rightEye.getRadius() - rightInnerEye.getRadius()));
        this.rightInnerEye.setCenterY(this.rightEye.getCenterY() + normalizeDirection.y * (rightEye.getRadius() - rightInnerEye.getRadius()));
    }

    public void grow() {
        Circle mainView = entity.getViewComponent().getChild(0, Circle.class);

        double centerX = mainView.getCenterX();
        double radius = mainView.getRadius();
        double eyeRadius = radius * 0.2;
        double eyeInnerRadius = radius * 0.1;

        this.rightEye.setRadius(eyeRadius);
        this.rightEye.setCenterX(centerX + radius * 0.4);
        this.rightEye.setCenterY(this.rightEye.getCenterY() + 1.0);

        this.leftEye.setRadius(eyeRadius);
        this.leftEye.setCenterX(centerX - radius * 0.4);
        this.leftEye.setCenterY(this.leftEye.getCenterY() + 1.0);

        this.leftInnerEye.setRadius(eyeInnerRadius);
        this.rightInnerEye.setRadius(eyeInnerRadius);
    }
}
