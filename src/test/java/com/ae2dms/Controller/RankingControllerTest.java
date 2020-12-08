package com.ae2dms.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

public class RankingControllerTest extends ApplicationTest {
    RankingController controller;
    Parent root;
    Stage stage;

    @Start
    public void start(Stage RankingStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface/Ranking.fxml"));
        root = loader.load();
        RankingStage.setScene(new Scene(root));
        RankingStage.show();
        controller = loader.getController();
        controller.init(RankingStage);
        stage = RankingStage;
    }


    /**
     * Ranking:
     Time: 19/11/2020 17:34:03
     Id: 1
     MapSetName: Example Game!
     MovesCount: 1279
     ---

     Time: 25/11/2020 16:47:47
     Id: 2
     MapSetName: Example Game!
     MovesCount: 1544
     ---


     Time: 26/11/2020 19:36:59
     Id: 3
     MapSetName: Example Game!
     MovesCount: 1292
     ---

     */
    @Test
    void RankingDisplay(){
        FxAssert.verifyThat("#rank1", (Text text)->{
            boolean res;
            System.out.println("---\n" + text.getText() + "---\n");
            res = ("Time: 19/11/2020 17:34:03\n" +
                    "MovesCount: 1279").equals(text.getText());
            return res;
        });
    }

    @Test
    void backToDefaultPage() throws IOException {
        clickOn("#confirm");
        // Redirect to Sokoban.fxml
        FxAssert.verifyThat("#Sokoban", (TilePane pane) -> {
            return pane.isVisible();
        });
    }




}