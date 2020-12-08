package com.ae2dms;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {
    Level level;
    GameGrid objectsGrid;
    GameGrid diamondsGrid;
    GameGrid combineGrid;
    GameGrid DefaultObjectsGrid;
    GameGrid DefaultDiamondsGrid;
    String name;
    int index;
    int numberOfDiamonds = 0;
    Point keeperPosition = new Point(0, 0);
    Point DefaultKeeperPosition = new Point(0, 0);
    InputStream input;
    GameEngine gameEngine;

    /**
     * The debugLevel Map (as follow):
     * WWWWWWWWWWWWWwWWWWWW
     * Ws cd   G          W
     * W       G          W
     * wwwwwwwwWwwwwwwwwwww
     */

    @BeforeEach
    void setUp() {
        // e.g.: levelName = "MapSetGame", levelIndex = 1, List<String> raw_level( of level 0)
        input = getClass().getClassLoader().getResourceAsStream("debugLevel.skb");
        gameEngine = new GameEngine(input,true);
        level = gameEngine.getLevels().get(0);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getGatePositions() {
        //gates position: (1,8) && (2,8)
        HashMap<Integer, Integer> gatePositions = level.getGatePositions();
        Assertions.assertEquals(8,gatePositions.get(1));
        Assertions.assertEquals(8,gatePositions.get(2));
    }

    @Test
    void isComplete() {
        /**
         * The debugLevel Map (as follow):
         * WWWWWWWWWWWWWwWWWWWW
         * Ws cd   G          W
         * W       G          W
         * wwwwwwwwWwwwwwwwwwww
         */
        gameEngine.move(new Point(0,1)); // move right
        /**
         * The debugLevel Map (as follow):
         * WWWWWWWWWWWWWwWWWWWW
         * W scd   G          W
         * W       G          W
         * wwwwwwwwWwwwwwwwwwww
         */
        Assertions.assertEquals(level.isComplete(),false);

        gameEngine.move(new Point(0,1)); // move right
        /**
         * The debugLevel Map (as follow):
         * WWWWWWWWWWWWWwWWWWWW
         * W  so   G          W
         * W       G          W
         * wwwwwwwwWwwwwwwwwwww
         * Completed!
         */
         Assertions.assertEquals(level.isComplete(),true);
    }

    @Test
    void getName() {
        Assertions.assertEquals("Just this level", level.getName());
    }

    @Test
    void getIndex() {
        Assertions.assertEquals(1, level.getIndex());
    }

    @Test
    void getKeeperPosition() {
        // The initial point of keeper -> (1,1)
        Assertions.assertEquals(1,(int) level.getKeeperPosition().getX());
        Assertions.assertEquals(1,(int) level.getKeeperPosition().getY());
    }

    @Test
    void getDefaultKeeperPosition() {
        // The initial point of keeper -> (1,1)
        Assertions.assertEquals(1,(int) level.getDefaultKeeperPosition().getX());
        Assertions.assertEquals(1,(int) level.getDefaultKeeperPosition().getY());
    }

    @Test
    void setKeeperPosition() {
        level.setKeeperPosition(new Point(1,8));
        Assertions.assertEquals(1, (int) level.getKeeperPosition().getX());
        Assertions.assertEquals(8, (int) level.getKeeperPosition().getY());
    }

    @Test
    void getTargetObject() {
        Point keeper = new Point(1,1);
        Point delta = new Point(0,2);// e.g. move right by 2
        // Then, its target should be CRATE
        Assertions.assertEquals('C', level.getTargetObject(keeper, delta).symbol);
    }

    @Test
    void objectToString() {
        String output = "WWWWWWWWWWWWWWWWWWWW\n" +
                "WS C               W\n" +
                "W                  W\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        Assertions.assertEquals(output, level.ObjectToString());
    }

    @Test
    void combineToString() {
        String output1 = "WWWWWWWWWWWWWWWWWWWW\n" +
                "WS CD   G          W\n" +
                "W       G          W\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        Assertions.assertEquals(output1, level.CombineToString());

        gameEngine.move(new Point(0,1)); // move right
        String output2 = "WWWWWWWWWWWWWWWWWWWW\n" +
                "W SCD   G          W\n" +
                "W       G          W\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        Assertions.assertEquals(output2, level.CombineToString());

        gameEngine.move(new Point(0,1)); // move right
        //CRATE ON DIAMOND
        String output3 = "WWWWWWWWWWWWWWWWWWWW\n" +
                "W  SO   G          W\n" +
                "W       G          W\n" +
                "WWWWWWWWWWWWWWWWWWWW\n";
        Assertions.assertEquals(output3, level.CombineToString());
    }

    @Nested
    @DisplayName("Test For Level Iterator")
    class LevelIteratorTest{
        @BeforeEach
        void setUp(){
            int row = 20;
            int col = 20;
        }
        @AfterEach
        void tearDown() {
        }
        @Test
        void next() {
            Assertions.assertEquals(level.iterator().next(), level.combineGrid.getGameObjectAt(1,0));
        }
    }

}