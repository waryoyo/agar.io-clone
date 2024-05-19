package org.example.agarioclone.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;
import static org.example.agarioclone.AgarioApp.WINDOW_HEIGHT;
import static org.example.agarioclone.AgarioApp.WINDOW_WIDTH;

public class CameraComponent extends Component {

    public void onAdded() {
        super.onAdded();
        //getGameScene().getViewport().bindToEntity(entity, (int)(WINDOW_WIDTH / 2), (int)(WINDOW_HEIGHT / 2));
    }

    public void updateCamera(Entity entity, boolean updateZoom){
        double zoom = FXGL.getWorldProperties().getDouble("zoom");
        Circle playerView = entity.getViewComponent().getChild(0, Circle.class);

        int width = (int)(WINDOW_WIDTH / zoom);
        int height = (int)(WINDOW_HEIGHT / zoom);
        getGameScene().getViewport().setX(entity.getX() + playerView.getRadius() - width / 2.0);
        getGameScene().getViewport().setY(entity.getY() + playerView.getRadius() - height / 2.0);

        if (updateZoom)
            getGameScene().getViewport().setZoom(zoom);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        updateCamera(entity, false);


    }
}
