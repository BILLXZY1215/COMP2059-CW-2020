package com.ae2dms.Controller;

import com.ae2dms.GameEngine;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuControllerTest extends ApplicationTest {
    Stage stage;
    MenuController controller;
    GridPane gameGrid;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Start
    public void start(Stage primaryStage) throws Exception {
        gameGrid = new GridPane();
        GridPane root = new GridPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface/Menu.fxml"));
        Parent menuBar = loader.load();
        root.add(menuBar, 0, 0);
        root.add(gameGrid, 0, 1);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        controller = loader.getController();
        controller.getUndo().setDisable(true);
        controller.getMusic().setSelected(true);
        controller.setMusic(false);
        // No Music At Start
        controller.getDebug().setSelected(true);
        controller.setGameGrid(gameGrid);
        controller.loadDefaultDebugFile(primaryStage);
        controller.level_step_fresh();
        stage = primaryStage;
    }

    @Test
    @Order(1)
    void ShowAbout() {
        moveTo("#AboutMenu");
        clickOn("#AboutMenu");
        moveTo("#About");
        clickOn("#About");
        FxAssert.verifyThat("#AboutText",(Text text)-> {
            boolean res;
            res = text.getFont().getSize() == 20;
            res = res && text.getFont().getFamily().equals("Phosphate");
            res = res && text.getText().equals("Game created by Zeyu XIONG\n");
            return res;
        });
        FxAssert.verifyThat("#AboutVBox",(VBox vBox)-> {
            boolean res;
            res = vBox.getBackground().equals(Background.EMPTY);
            res = res && vBox.getAlignment().equals(Pos.CENTER);
            return res;
        });
    }


    @Test
    @Order(2)
    void ShowLoadGame() {
        moveTo("#file");
        clickOn("#file");
        clickOn("#LoadGame");
    }

    @Test
    @Order(3)
    void ShowSaveGame() {
        moveTo("#file");
        clickOn("#file");
        clickOn("#SaveGame");
    }


    @Test
    @Order(4)
    void ShowExitDialog() {
        moveTo("#file");
        clickOn("#file");
        clickOn("#Exit");
        moveTo("#CANCEL");
        clickOn("#CANCEL");
    }


    @Test
    @Order(5)
    void undo() {
        int y = (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY();
        sleep(1000);
        press(KeyCode.LEFT);
        release(KeyCode.LEFT);
        Assertions.assertEquals(y-1, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
        sleep(1000);
        press(KeyCode.LEFT);
        release(KeyCode.LEFT);
        Assertions.assertEquals(y-2, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
        moveTo("#level");
        clickOn("#level");
        clickOn("#Undo");
        sleep(1000);
        Assertions.assertEquals(y-1, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
        moveTo("#level");
        clickOn("#level");
        clickOn("#Undo");
        sleep(1000);
        Assertions.assertEquals(y, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
        moveTo("#level");
        clickOn("#level");
        clickOn("#Undo");
        sleep(1000);
        Assertions.assertEquals(y, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
    }

    @Test
    @Order(6)
    void undo_FastKey(){
        int y = (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY();
        sleep(1000);
        press(KeyCode.LEFT);
        release(KeyCode.LEFT);
        Assertions.assertEquals(y-1, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
        sleep(1000);
        press(KeyCode.LEFT);
        release(KeyCode.LEFT);
        Assertions.assertEquals(y-2, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
        press(KeyCode.B);
        release(KeyCode.B);
        sleep(1000);
        press(KeyCode.B);
        sleep(1000);
        Assertions.assertEquals(y, (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY());
        sleep(500);
    }

    @Test
    @Order(7)
    void ToggleMusic() {
        System.out.println("It Just Works!");
        moveTo("#level");
        clickOn("#level");
        moveTo("#Music");
    }


    @Test
    @Order(8)
    void ToggleDebug() {
        moveTo("#level");
        clickOn("#level");
        moveTo("#Debug");
        clickOn("#Debug");
        clickOn("#level");
        clickOn("#Debug");
    }



    @Test
    @Order(9)
    void reset() {
        int x = (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getX();
        int y = (int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY();
        sleep(1000);
        press(KeyCode.UP);
        release(KeyCode.UP);
        sleep(1000);
        Assertions.assertEquals(x-1,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(y,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY() );
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        sleep(1000);
        Assertions.assertEquals(x,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(y,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY() );
        press(KeyCode.LEFT);
        release(KeyCode.LEFT);
        sleep(1000);
        Assertions.assertEquals(x,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(y-1,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY() );
        press(KeyCode.LEFT);
        release(KeyCode.LEFT);
        sleep(1000);
        Assertions.assertEquals(x,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(y-2,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY() );
        press(KeyCode.LEFT);
        release(KeyCode.LEFT);
        sleep(1000);
        Assertions.assertEquals(x,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(y-3,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY() );
        moveTo("#level");
        clickOn("#level");
        moveTo("#ResetLevel");
        clickOn("#ResetLevel");
        Assertions.assertEquals(x,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(y,(int)controller.getGameEngine().getCurrentLevel().getKeeperPosition().getY() );
        sleep(1000);
    }


    @Test
    @Order(11)
    void zombie() {
        moveTo("#modes");
        clickOn("#modes");
        moveTo("#Zombie");
        clickOn("#Zombie");
        sleep(1000);
    }

    @Test
    @Order(12)
    void plant() {
        moveTo("#modes");
        clickOn("#modes");
        moveTo("#Plant");
        clickOn("#Plant");
        sleep(1000);
    }

    @Test
    @Order(13)
    void getPower() {
        press(KeyCode.UP);
        release(KeyCode.UP);
        sleep(500);
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        sleep(500);
        //Check if the power is get
        Assertions.assertEquals(3,controller.getGameEngine().getPowerCount());
    }

    @Test
    @Order(14)
    void getNextLevel() {
        for(int i = 0; i< 5; i++){
            press(KeyCode.LEFT);
            release(KeyCode.LEFT);
            sleep(500);
            if(i==4){
                Assertions.assertEquals(2, controller.getGameEngine().getCurrentLevel().getIndex());
            }else{
                Assertions.assertEquals(1, controller.getGameEngine().getCurrentLevel().getIndex());
            }
        }
    }

}