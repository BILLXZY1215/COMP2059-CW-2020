/**
 * @author Zeyu Xiong
 */

package com.ae2dms.Starter;

import com.ae2dms.Controller.GameStartController;
import com.ae2dms.GameEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameStart extends Application {
    GameStartController controller;
    private Stage defaultStage;

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * This Method initializes the javaFX Application
     *
     * @param defaultStage the Stage of the game
     * @throws Exception load/controller.init may throw exception
     */
    @Override
    public void start(Stage defaultStage) throws Exception {
        this.defaultStage = defaultStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface/Sokoban.fxml"));
        Parent root = loader.load();
        defaultStage.getIcons().add(new Image("/img/icon.png"));
        defaultStage.setTitle(GameEngine.GAME_NAME);
        defaultStage.setScene(new Scene(root));
        defaultStage.setHeight(600);
        defaultStage.setWidth(600);
        defaultStage.setResizable(false);
        defaultStage.initStyle(StageStyle.valueOf("UTILITY"));
        defaultStage.show();
        controller = loader.getController();
        controller.init(defaultStage);
    }


    /**
     * This Method Restart the javaFX Application
     * if restart, we do not need to set Height, Width, Title, Icon, Resizable
     *
     * @param defaultStage
     * @throws Exception
     */
    public void reStart(Stage defaultStage) throws Exception {
        this.defaultStage = defaultStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface/Sokoban.fxml"));
        Parent root = loader.load();
        defaultStage.setScene(new Scene(root));
        defaultStage.show();
        controller = loader.getController();
        controller.init(defaultStage, true);
    }

    /**
     * This Method "manually" triggers start(stage)
     *
     * @param primaryStage the Stage of the game
     * @throws Exception because start may throw Exception
     */
    public void showWindow(Stage primaryStage) throws Exception {
        reStart(primaryStage);
    }

}
