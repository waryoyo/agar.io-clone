package org.example.agarioclone.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static org.example.agarioclone.AgarioApp.WINDOW_HEIGHT;
import static org.example.agarioclone.AgarioApp.WINDOW_WIDTH;

public class CameraComponent extends Component {
    public void onAdded() {
        super.onAdded();
//        getGameScene().getViewport().bindToEntity(entity, (int)(WINDOW_WIDTH / 2), (int)(WINDOW_HEIGHT / 2));
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        Circle playerView = entity.getViewComponent().getChild(0, Circle.class);

        getGameScene().getViewport().setX(entity.getX() + playerView.getRadius() - WINDOW_WIDTH/2.0);
        getGameScene().getViewport().setY(entity.getY() + playerView.getRadius() - WINDOW_HEIGHT/2.0);

    }
}
