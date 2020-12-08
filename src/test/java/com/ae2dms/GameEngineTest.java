package com.ae2dms;

import org.junit.jupiter.api.*;

import java.awt.*;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    GameEngine gameEngine;
    InputStream input;

    /** First Level:
     * MapSetName: Game debug!
     * LevelName: Just this level
     * WWWWWWWWWWWWW WWWWWW
     * W    W             W
     * W    W D  C  S     W
     * w    w      WWWWWWWW
     * wwwwwwwwWwwwwwwwwwww
     */
    @BeforeEach
    void setUp() {
        // Load Test File "debugGame.skb"
        input = getClass().getClassLoader().getResourceAsStream("debugGame.skb");
        gameEngine = new GameEngine(input,true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addHash() {
        gameEngine.addHash("RIGHT", "FLOOR");
        gameEngine.addHash("LEFT", "FLOOR");
        Assertions.assertEquals("RIGHT,FLOOR",gameEngine.getBackMap().get(1));
        Assertions.assertEquals("LEFT,FLOOR",gameEngine.getBackMap().get(2));
    }

    @Test
    void deleteStep() {
        gameEngine.addHash("RIGHT", "FLOOR");
        Assertions.assertEquals("RIGHT,FLOOR",gameEngine.getBackMap().get(1));
        gameEngine.deleteStep();
        Assertions.assertEquals(null, gameEngine.getBackMap().get(1));
    }

    @Test
    void moveLeft() {
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 13)
         */
        gameEngine.move(new Point(0, -1)); // Move Left
        Assertions.assertEquals(2, (int) gameEngine.getCurrentLevel().getKeeperPosition().getX());
        Assertions.assertEquals(12, (int) gameEngine.getCurrentLevel().getKeeperPosition().getY());
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C S      W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 12)
         */
    }

    @Test
    void moveRight(){
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 13)
         */
        gameEngine.move(new Point(0,-2)); // Move Left by 2
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  CS       W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 11)
         */
        gameEngine.move(new Point(0,1)); // Move Right by 1
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C S      W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 12)
         */
        Assertions.assertEquals(2, (int)gameEngine.getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(12, (int)gameEngine.getCurrentLevel().getKeeperPosition().getY() );
    }

    @Test
    void moveUp(){
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 13)
         */
        gameEngine.move(new Point(-1,0)); // Move Up by 1
        Assertions.assertEquals(1, (int)gameEngine.getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(13, (int)gameEngine.getCurrentLevel().getKeeperPosition().getY() );
    }

    @Test
    void moveOutCorner(){
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W       S     W
         * W    W D  C        W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (1, 13)
         */

        gameEngine.move(new Point(-1,0)); // Move Up by 1
        gameEngine.move(new Point(-1,0)); // Move Up by 1
        /** Not possible to move out the map
         * WWWWWWWWWWWWWSWWWWWW
         * W    W             W
         * W    W D  C        W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (0, 13) ===> should stay at (1, 13)
         */
        Assertions.assertEquals(1, (int)gameEngine.getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(13, (int)gameEngine.getCurrentLevel().getKeeperPosition().getY() );
    }

    @Test
    void moveDown(){
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 13)
         */
        gameEngine.move(new Point(0,-2)); // Move Left by 2
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  CS       W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 11)
         */
        gameEngine.move(new Point(1,0)); // Move Down by 1
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C        W
         * w    w     SWWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (3, 11)
         */
        Assertions.assertEquals(3, (int)gameEngine.getCurrentLevel().getKeeperPosition().getX() );
        Assertions.assertEquals(11, (int)gameEngine.getCurrentLevel().getKeeperPosition().getY() );
    }






    @Test
    void undo() {
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 13)
         */
        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1

        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  CS       W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 11)
         * C -> (2, 10)
         */
        Assertions.assertEquals('S', gameEngine.getCurrentLevel().getTargetObject(new Point(2,11), new Point(0,0)).symbol);
        Assertions.assertEquals('C', gameEngine.getCurrentLevel().getTargetObject(new Point(2,10), new Point(0,0)).symbol);
        Assertions.assertEquals(' ', gameEngine.getCurrentLevel().getTargetObject(new Point(2,9), new Point(0,0)).symbol);
        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D CS        W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 10)
         * C -> (2, 9)
         */
        Assertions.assertEquals(' ', gameEngine.getCurrentLevel().getTargetObject(new Point(2,11), new Point(0,0)).symbol);
        Assertions.assertEquals('S', gameEngine.getCurrentLevel().getTargetObject(new Point(2,10), new Point(0,0)).symbol);
        Assertions.assertEquals('C', gameEngine.getCurrentLevel().getTargetObject(new Point(2,9), new Point(0,0)).symbol);

        gameEngine.undo();

        /** Back!
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  CS       W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 11)
         * C -> (2, 10)
         */
        Assertions.assertEquals('S', gameEngine.getCurrentLevel().getTargetObject(new Point(2,11), new Point(0,0)).symbol);
        Assertions.assertEquals('C', gameEngine.getCurrentLevel().getTargetObject(new Point(2,10), new Point(0,0)).symbol);
        Assertions.assertEquals(' ', gameEngine.getCurrentLevel().getTargetObject(new Point(2,9), new Point(0,0)).symbol);
    }

    @Test
    void resetLevel() {
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 13)
         */
        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1

        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  CS       W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 11)
         * C -> (2, 10)
         */
        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1
        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D CS        W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 10)
         * C -> (2, 9)
         */

        gameEngine.resetLevel();

        /**
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         *
         * S -> (2, 13)
         */

        Assertions.assertEquals('S', gameEngine.getCurrentLevel().getTargetObject(new Point(2,13), new Point(0,0)).symbol);
        Assertions.assertEquals('C', gameEngine.getCurrentLevel().getTargetObject(new Point(2,10), new Point(0,0)).symbol);

    }


    @Test
    void getNextLevel() {
        /** Level 1
         * WWWWWWWWWWWWW WWWWWW
         * W    W             W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         */
        Assertions.assertEquals(1, gameEngine.getCurrentLevel().getIndex());
        Level nextLevel = gameEngine.getNextLevel();
        /** Level 2
         * WWWWWWWWWWWWW WWWWWW
         * W    W D  C        W
         * W    W D  C  S     W
         * w    w      WWWWWWWW
         * wwwwwwwwWwwwwwwwwwww
         */
        Assertions.assertEquals(2, nextLevel.getIndex());
        Assertions.assertEquals('S', nextLevel.getTargetObject(new Point(2,13), new Point(0,0)).symbol);
        Assertions.assertEquals('C', nextLevel.getTargetObject(new Point(2,10), new Point(0,0)).symbol);
        Assertions.assertEquals('C', nextLevel.getTargetObject(new Point(1,10), new Point(0,0)).symbol);
    }

    @Test
    void getCurrentLevel() {
        Assertions.assertEquals(1, gameEngine.getCurrentLevel().getIndex());
        String level1 = "WWWWWWWWWWWWW WWWWWW\n" +
                "W    W       P     W\n" +
                "W    W D  C  S     W\n" +
                "W    W      WWWWWWWW\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        Assertions.assertEquals(level1, gameEngine.getCurrentLevel().CombineToString());
    }


    @Test
    void getMapSetName() {
        Assertions.assertEquals("Game debug!", gameEngine.getMapSetName());
    }

    @Test
    void getLevels() {
        // debugGame.skb --> 5 levels
        String level1 = "WWWWWWWWWWWWW WWWWWW\n" +
                "W    W       P     W\n" +
                "W    W D  C  S     W\n" +
                "W    W      WWWWWWWW\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        String level2 = "WWWWWWWWWWWWW WWWWWW\n" +
                "W    W D  C        W\n" +
                "W    W D  C  S     W\n" +
                "W    W      WWWWWWWW\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        String level3 = "WWWWWWWWWWWWW WWWWWW\n" +
                "W    W             W\n" +
                "W    W    C  S     W\n" +
                "W    W      WWWWWWWW\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        String level4 = "WWWWWWWWWWWWW WWWWWW\n" +
                "W    W             W\n" +
                "W    W D  C  S     W\n" +
                "W    W      WWWWWWWW\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        String level5 = "WWWWWWWWWWWWW WWWWWW\n" +
                "W    W             W\n" +
                "W    W D  C  S     W\n" +
                "W    W      WWWWWWWW\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        Assertions.assertEquals(5, gameEngine.getLevels().size());
        Assertions.assertEquals(level1, gameEngine.getLevels().get(0).CombineToString());
        Assertions.assertEquals(level2, gameEngine.getLevels().get(1).CombineToString());
        Assertions.assertEquals(level3, gameEngine.getLevels().get(2).CombineToString());
        Assertions.assertEquals(level4, gameEngine.getLevels().get(3).CombineToString());
        Assertions.assertEquals(level5, gameEngine.getLevels().get(4).CombineToString());
    }

    @Test
    void getMovesCount() {
        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1
        Assertions.assertEquals(1, gameEngine.getMovesCount());
        gameEngine.undo();
        // undo should add moves count as well (you must pay for it! Not undo freely)
        Assertions.assertEquals(2, gameEngine.getMovesCount());

    }

    @Test
    void getUndoCount() {
        gameEngine.setBackKey("RIGHT");
        gameEngine.move(new Point(0,-1)); // Move Left by 1
        Assertions.assertEquals(1, gameEngine.getUndoCount());
        // Only when undoCount > 0, undo is valid.
        gameEngine.undo();
        Assertions.assertEquals(0, gameEngine.getUndoCount());
    }

}