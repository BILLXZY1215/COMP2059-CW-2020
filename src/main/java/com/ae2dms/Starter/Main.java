/**
 * @author Zeyu Xiong
 */

package com.ae2dms.Starter;

import com.ae2dms.Controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    private Stage primaryStage;
    private GridPane gameGrid;
    private MenuController controller;
    private InputStream input;
    private boolean flag = false;

    // Design Pattern: Singleton
    private static Main main = new Main();
    private Main(){}

    /**
     * @return the instance of Main
     */
    public static Main getInstance(){
        return main;
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This Method initializes the javaFX Application
     * @param primaryStage the Stage of the game
     * @throws Exception load/controller.init may throw exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        gameGrid = new GridPane();
        GridPane root = new GridPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface/Menu.fxml"));
        Parent menuBar = loader.load();
        root.add(menuBar, 0, 0);
        root.add(gameGrid, 0, 1);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        controller = loader.getController();
        if(flag){ //showWindow has parameter "input" -> LoadGame from the default Page
            controller.init(primaryStage,gameGrid,input);
        }else{
            controller.init(primaryStage,gameGrid);
        }
    }

    /**
     * This Method "manually" triggers start(stage)
     * Load Game from Menu Bar
     * @param primaryStage the Stage of the game
     * @throws Exception because start may throw Exception
     */
    public void showWindow(Stage primaryStage) throws Exception {
        start(primaryStage);
    }

    /**
     * This Method "manually" triggers start(stage)
     * Load Game from Default Page
     * @param primaryStage the Stage of the game
     * @param input The inputStream of a file
     * @throws Exception because start may throw Exception
     */
    public void showWindow(Stage primaryStage, InputStream input) throws Exception {
        this.input = input;
        this.flag = true;
        start(primaryStage);
    }

}
