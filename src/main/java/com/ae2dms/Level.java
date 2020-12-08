/**
 * @author Zeyu Xiong
 */

package com.ae2dms;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class Level implements Iterable<GameObject>, Cloneable {
    public GameGrid objectsGrid;
    public GameGrid diamondsGrid;
    public GameGrid combineGrid;
    public final GameGrid DefaultObjectsGrid;
    public final GameGrid DefaultDiamondsGrid;
    private final String name;
    private final int index;
    private int numberOfDiamonds = 0;
    private Point keeperPosition = new Point(0, 0);
    private Point DefaultKeeperPosition = new Point(0, 0);

    /**
     * This constructor initializes the position of objectGrid, diamondGrid and combineGrid (objectGrid + the position of diamond),
     * and initializes DefaultDiamondGrid and DefaultObjectGrid (For Reset)
     * @param levelName The name of this level
     * @param levelIndex The index of this level
     * @param raw_level The inputStream of the map of this map
     */
    public Level(String levelName, int levelIndex, List<String> raw_level) {
        if (GameEngine.isDebugActive()) { // Only Initializing part has a change to trigger this method.
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
            System.out.println(raw_level); // raw_level: 20 columns, each column contains one row of the 'game grid'
        }

        name = levelName;
        index = levelIndex;

        int rows = raw_level.size(); //the size of raw_level represents the exactly number of rows of the 'game grid'
        int columns = raw_level.get(0).trim().length(); //get columns of the 'game grid'

        DefaultObjectsGrid = new GameGrid(rows, columns);
        DefaultDiamondsGrid = new GameGrid(rows, columns);

        objectsGrid = new GameGrid(rows, columns);
        diamondsGrid = new GameGrid(rows, columns);
        combineGrid = new GameGrid(rows, columns);

        for (int row = 0; row < raw_level.size(); row++) {
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                GameObject curTile = GameObject.fromChar(raw_level.get(row).charAt(col));

                if (curTile == GameObject.CRATE_ON_DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(GameObject.DIAMOND, row, col);
                    DefaultDiamondsGrid.putGameObjectAt(GameObject.DIAMOND, row, col); //Default: will not change at currentLevel
                    objectsGrid.putGameObjectAt(GameObject.CRATE, row,col);
                    DefaultObjectsGrid.putGameObjectAt(GameObject.CRATE, row, col);
                    combineGrid.putGameObjectAt(curTile, row, col);
                    curTile = null;
                    continue;
                }

                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    DefaultDiamondsGrid.putGameObjectAt(curTile, row, col); //Default: will not change at currentLevel
                    objectsGrid.putGameObjectAt(GameObject.FLOOR, row, col); // Diamond -> FLOOR : Clear Way for CRATE/Keeper in
                    DefaultObjectsGrid.putGameObjectAt(GameObject.FLOOR, row, col); //Default: will not change at currentLevel
                    combineGrid.putGameObjectAt(curTile, row, col);
                    curTile = null;
                    continue;
                }

                if(curTile == GameObject.GATE || curTile == GameObject.POWER){ // objectGrid -> add FLOOR
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    DefaultDiamondsGrid.putGameObjectAt(curTile, row, col); //Default: will not change at currentLevel
                    objectsGrid.putGameObjectAt(GameObject.FLOOR, row, col); // Diamond -> FLOOR : Clear Way for CRATE/Keeper in
                    DefaultObjectsGrid.putGameObjectAt(GameObject.FLOOR, row, col); //Default: will not change at currentLevel
                    combineGrid.putGameObjectAt(curTile, row, col);
                    curTile = null;
                    continue;
                }

                if (curTile == GameObject.KEEPER) {
                    keeperPosition = new Point(row, col);
                    DefaultKeeperPosition = new Point(row,col);
                }
                objectsGrid.putGameObjectAt(curTile, row, col);
                DefaultObjectsGrid.putGameObjectAt(curTile, row, col); //Default: will not change at currentLevel
                combineGrid.putGameObjectAt(curTile, row, col);
                curTile = null;
            }
        }
    }

    /**
     * This Method updates the combineGrid (adds diamond on objectGrid, and checks whether the crate is on diamond or not)
     */
    public void updateCombineGrid() {
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                GameObject temp = objectsGrid.getGameObjectAt(col, row);
                combineGrid.putGameObjectAt(temp, col, row);
                if (diamondsGrid.getGameObjectAt(col, row) == GameObject.DIAMOND) {
                    if(temp == GameObject.CRATE) {
                        combineGrid.putGameObjectAt(GameObject.CRATE_ON_DIAMOND, col, row); //ADD CRATE_ON_DIAMOND
                    }else{
                        combineGrid.putGameObjectAt(GameObject.DIAMOND, col, row); //Otherwise, Add DIAMOND
                    }
                }else if(diamondsGrid.getGameObjectAt(col, row) == GameObject.GATE){
                    combineGrid.putGameObjectAt(GameObject.GATE, col, row); //ADD GATE
                }else if(diamondsGrid.getGameObjectAt(col, row) == GameObject.POWER){
                    combineGrid.putGameObjectAt(GameObject.POWER, col, row); //ADD POWER
                }
            }
        }
    }


    /**
     * This Method gets a list of all gates positions
     * @return A list of gates positions
     */
    public HashMap<Integer, Integer> getGatePositions() {
        HashMap<Integer, Integer> gatePositions = new HashMap<Integer, Integer>();
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if(diamondsGrid.getGameObjectAt(col, row) == GameObject.GATE){
                    gatePositions.put(col, row);
                }
            }
        }
        return gatePositions;
    }


    /**
     * This Method checks whether the game is completed,
     * by checking if all diamonds have been covered by crate
     * @return a boolean value: True -> Level Completed; False -> Level not completed
     */
    boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE && diamondsGrid.getGameObjectAt(col, row) == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }
        return cratedDiamondsCount >= numberOfDiamonds;
    }

    /**
     * @return level name
     */
    public String getName() {
        return name;
    }

    /**
     * @return level index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the position of the keeper
     */
    public Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * @return the default position of the keeper (first enter this level)
     */
    Point getDefaultKeeperPosition() {
        return DefaultKeeperPosition;
    }

    /**
     * @param point the point sets to the keeper
     */
    public void setKeeperPosition(Point point) {
        keeperPosition = point;
    }


    /**
     * This Method gets the game object that a given game object moves delta steps
     * @param source source position
     * @param delta from source position, move delta position to get a new position
     * @return the targeted game object
     */
    GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }


    /**
     * @return the string map of objectsGrid
     */
    public String ObjectToString() {
        return objectsGrid.toString();
    }


    /**
     * This Method updates the combine grid, and returns the string map of combineGrid
     * @return the string map of combineGrid
     */
    public String CombineToString() {
        updateCombineGrid();
        return combineGrid.toString();
    }


    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    /**
     * This Class iterates all Game objects within a level
     */
    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;
        @Override
        public boolean hasNext() {
            return !(row == combineGrid.ROWS - 1 && column == combineGrid.COLUMNS);
        }
        @Override
        public GameObject next() {
            if (column >= combineGrid.COLUMNS) {
                //Traverse
                column = 0;
                row++;
            }
            updateCombineGrid();
            GameObject retObj = combineGrid.getGameObjectAt(column,row);
            GameObject Obj = objectsGrid.getGameObjectAt(column,row);
            column++;
            if(Obj == GameObject.KEEPER){
                return Obj; // KEEPER_On_Diamond
            }else{
                return retObj;
            }
        }

        /**
         * @return the current position of the level iterator
         */
        public Point getCurrentPosition() {
            return new Point(column, row);
        }

    }
}