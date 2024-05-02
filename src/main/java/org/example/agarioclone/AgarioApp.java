package org.example.agarioclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import  java.awt.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AgarioApp extends GameApplication {

    static public int window_width = 2000;
    static public int window_height = 2000;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setFullScreenAllowed(true);
//        gameSettings.setFullScreenFromStart(true);

        // TODO: make it full screen dynamically
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // int width = (int)screenSize.getWidth();
        // int height = (int)screenSize.getHeight();

        gameSettings.setWidth(window_width);
        gameSettings.setHeight(window_height);

    }
}