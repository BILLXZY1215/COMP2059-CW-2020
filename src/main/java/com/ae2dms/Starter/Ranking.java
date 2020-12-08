/**
 * @author Zeyu Xiong
 */

package com.ae2dms.Starter;

import com.ae2dms.Controller.RankingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Ranking extends Application {
    RankingController controller;

    // Design Pattern: Singleton
    private static Ranking ranking = new Ranking();
    private Ranking(){}

    /**
     * @return the instance of Ranking
     */
    public static Ranking getInstance(){
        return ranking;
    }

    public static void main(String[] args){
        launch(args);
    }

    /**
     * This Method initializes the javaFX Application
     * @param RankingStage the Stage of the game
     * @throws Exception load/controller.init may throw exception
     */
    @Override
    public void start(Stage RankingStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface/Ranking.fxml"));
        Parent root = loader.load();
        RankingStage.setScene(new Scene(root));
        RankingStage.show();
        controller = loader.getController();
        controller.init(RankingStage);
    }

    /**
     * This Method "manually" triggers start(stage)
     * @param primaryStage the Stage of the game
     * @throws Exception because start may throw Exception
     */
    public void showWindow(Stage primaryStage) throws Exception {
        start(primaryStage);
    }
}
