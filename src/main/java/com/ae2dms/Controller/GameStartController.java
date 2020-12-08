/**
 * @author Zeyu Xiong
 */
package com.ae2dms.Controller;
import com.ae2dms.Starter.Main;
import com.ae2dms.Starter.Ranking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameStartController {
    private Stage defaultStage;
    private File loadFile = null;
    private boolean restart = false;

    @FXML
    private Button btn_1;

    @FXML
    private Button btn_2;

    @FXML
    private Button btn_3;

    /**
     * @param defaultStage Set the Stage in model to the controller
     */
    public void init(Stage defaultStage) {
        this.defaultStage = defaultStage;
    }

    /**
     * @param restart sets the mode of start
     */
    public void init(Stage defaultStage, boolean restart) {
        this.defaultStage = defaultStage;
        this.restart = restart;
    }

    /**
     * This Method gets the instance of Main,
     * and loadGameFile,
     * then calls the method showWindow in main
     * @param event event
     * @throws Exception because showWindow and loadGameFile may throw exceptions
     */
    @FXML
    void load(ActionEvent event) throws Exception {
        Main main = Main.getInstance(); // Design Pattern: Singleton
        loadGameFile();
        main.showWindow(defaultStage, new FileInputStream(loadFile));
    }


    /**
     * This Method gets the instance of Ranking,
     * then calls the method showWindow in ranking
     * @param event event
     * @throws Exception because showWindow may throw exceptions
     */
    @FXML
    void rank(ActionEvent event) throws Exception {
        Ranking ranking = Ranking.getInstance(); // Design Pattern: Singleton
        ranking.showWindow(defaultStage);
    }


    /**
     * This Method will be called after clicking on "Start Game" button,
     * it gets the instance of Main,
     * then calls the method showWindow in ranking
     * @param event event
     * @throws Exception because showWindow may throw exceptions
     */
    @FXML
    void start(ActionEvent event) throws Exception {
        Main main = Main.getInstance(); // Design Pattern: Singleton
        if(restart){
            main.showWindow(defaultStage, getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb"));
        }else{
            main.showWindow(defaultStage);
        }
    }

    /**
     * This method calls a file selector to load game file
     * @throws FileNotFoundException This exception will be thrown by the FileInputStream, FileOutputStream
     */
    private void loadGameFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban load file", "*.skb"));
        loadFile = fileChooser.showOpenDialog(defaultStage);
    }


}
