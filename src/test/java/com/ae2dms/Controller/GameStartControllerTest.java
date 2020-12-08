package com.ae2dms.Controller;

import com.ae2dms.GameEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameStartControllerTest extends ApplicationTest {

    GameStartController controller;
    Stage stage;

    @Start
    public void start(Stage defaultStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface/Sokoban.fxml"));
        Parent root = loader.load();
        defaultStage.getIcons().add(new Image("/img/icon.png"));
        defaultStage.setTitle(GameEngine.GAME_NAME);
        defaultStage.setScene(new Scene(root));
        defaultStage.setHeight(600);
        defaultStage.setWidth(600);
        defaultStage.setResizable(false);
        defaultStage.show();
        controller = loader.getController();
        controller.init(defaultStage);
        stage = defaultStage;
    }


    @Test
    @Order(1)
    void ClickRank(){
        clickOn("#btn_3");
        sleep(1000);
        // Redirect to Ranking.fxml
        FxAssert.verifyThat("#Ranking", (Pane pane) -> {
            return pane.isVisible();
        });
        moveTo("#confirm");
        clickOn("#confirm");
    }

    @Test
    @Order(2)
    void ClickLoad(){
        clickOn("#btn_2");
    }



}