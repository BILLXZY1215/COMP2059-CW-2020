package com.ae2dms;

import org.junit.jupiter.api.*;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class GameGridTest {
    GameGrid gameGrid;
    Point sourceLocation;
    Point delta;
    int col = 20;
    int row = 20;

    @BeforeEach
    void setUp() {

        gameGrid = new GameGrid(col, row);
        // Sample Data
        sourceLocation = new Point(1,2);
        delta = new Point(0,1);

        gameGrid.putGameObjectAt(GameObject.KEEPER,1,2);
        gameGrid.putGameObjectAt(GameObject.GATE,1,3);
        gameGrid.putGameObjectAt(GameObject.CRATE,8,4);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void translatePoint() {
        int x = (int) (sourceLocation.getX() + (int) delta.getX());
        int y = (int) (sourceLocation.getY() + (int) delta.getY());
        Point res = GameGrid.translatePoint(sourceLocation, delta);
        Assertions.assertEquals(x, (int)res.getX());
        Assertions.assertEquals(y, (int)res.getY());
    }

    @Test
    void putGameObjectAt() {
        // (1,2) -> KEEPER
        Assertions.assertEquals(gameGrid.getGameObjectAt(1,2),GameObject.KEEPER);
        // Override
        // (8,4) -> CRATE
        Assertions.assertEquals(gameGrid.getGameObjectAt(new Point(8,4)),GameObject.CRATE);
    }


    @Test
    void getGameObjectAt() {
        Assertions.assertEquals(gameGrid.getGameObjectAt(1,3),GameObject.GATE);
        //Override
        Assertions.assertEquals(gameGrid.getGameObjectAt(new Point(1,3)),GameObject.GATE);
    }

    @Test
    void getTargetFromSource() {
        //(1,2) + (0,1) = (1,3)
        GameObject obj = gameGrid.getTargetFromSource(sourceLocation, delta);
        Assertions.assertEquals(obj.symbol, 'G');
    }

    @Test
    void removeGameObjectAt() {
        gameGrid.removeGameObjectAt(new Point(1,3));
        //After remove, the object of (1,3) should be null
        Assertions.assertNull(gameGrid.getGameObjectAt(1,3));
    }


    @Test
    void isPointOutOfBounds() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Reflection: get private method
        Method isPointOutOfBounds =gameGrid.getClass().getDeclaredMethod("isPointOutOfBounds", int.class, int.class);
        isPointOutOfBounds.setAccessible(true);
        //  x < 0 || y < 0 || x >= COLUMNS || y >= ROWS
        Assertions.assertEquals(isPointOutOfBounds.invoke(gameGrid,-1,0), true);
        Assertions.assertEquals(isPointOutOfBounds.invoke(gameGrid,0,-1), true);
        Assertions.assertEquals(isPointOutOfBounds.invoke(gameGrid,col+1, 0), true);
        Assertions.assertEquals(isPointOutOfBounds.invoke(gameGrid,0, row+1), true);

        // Reflection: get private method
        Method isPointOutOfBoundsByPoint =gameGrid.getClass().getDeclaredMethod("isPointOutOfBounds", Point.class);
        isPointOutOfBoundsByPoint.setAccessible(true);
        //  x < 0 || y < 0 || x >= COLUMNS || y >= ROWS
        Assertions.assertEquals(isPointOutOfBoundsByPoint.invoke(gameGrid,new Point(-1,0)), true);
        Assertions.assertEquals(isPointOutOfBoundsByPoint.invoke(gameGrid,new Point(0,-1)), true);
        Assertions.assertEquals(isPointOutOfBoundsByPoint.invoke(gameGrid,new Point(col+1, 0)), true);
        Assertions.assertEquals(isPointOutOfBoundsByPoint.invoke(gameGrid,new Point(0, row+1)), true);

    }

    @Nested
    @DisplayName("Test for Grid Iterator")
    class GridIteratorTest{
        Iterator<GameObject> iterator;
        GameGrid gameGrid2;
        int row = 20;
        int col = 20;
        @BeforeEach
        void setUp(){
            gameGrid2 = new GameGrid(col, row);
            iterator = gameGrid2.iterator();
            // (0,1) -> KEEPER
            gameGrid2.putGameObjectAt(GameObject.KEEPER,0,1);
        }
        @AfterEach
        void tearDown() {
        }

        @Test
        void next(){
            // Get GameObject from (0,1) --> KEEPER
            Assertions.assertEquals(iterator.next(), gameGrid2.getGameObjectAt(1,0));
        }

        @Test
        void hasNext(){
            for(int i = 0; i < col; i++){
                for(int j = 0; j < row; j++){
                    iterator.next();
                    Assertions.assertEquals(iterator.hasNext(), true);
                }
            }
            Assertions.assertEquals(iterator.hasNext(), true);
            Assertions.assertEquals(iterator.hasNext(), true);
        }


    }

}