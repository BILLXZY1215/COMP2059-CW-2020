/**
 * @author Zeyu Xiong
 */

package com.ae2dms;

import java.awt.*;
import java.util.Iterator;

public class GameGrid implements Iterable {
    final int COLUMNS;
    final int ROWS;

    private final GameObject[][] gameObjects;

    /**
     * This constructor initializes a matrix of game object
     * @param columns the size of col
     * @param rows the size of row
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;

        // Initialize the array
        gameObjects = new GameObject[COLUMNS][ROWS];
    }

    /**
     * This Method returns a point which is calculated by (point + delta)
     * @param sourceLocation the source location point
     * @param delta the distance that a points could move
     * @return the new point after moving
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * This Method returns a GameObject in position of (point + delta)
     * @param source the source point position
     * @param delta the distance the point wants to move
     * @return the GameObject in position of (point + delta)
     */
    public GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * This Method returns a GameObject in position of (point + delta)
     * @param col the column of the target
     * @param row the row of the target
     * @return the target game object
     * @throws ArrayIndexOutOfBoundsException because the point is possibly outside the map
     */
    public GameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (GameEngine.isDebugActive()) {
                System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
        }

        return gameObjects[col][row];
    }

    /**
     * This Method gets the GameObject on the position of the point you wants to get
     * @param p the position of the point you wants to get
     * @return the GameObject ant the position of the point you wants to get
     */
    public GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }

    /**
     * This Method puts a game object at the specified position
     * @param gameObject  the game object you wants to put
     * @param x row
     * @param y col
     * @return a boolean value, true -> assign success, false -> assign failure
     */
    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        gameObjects[x][y] = gameObject;
        return gameObjects[x][y] == gameObject;
    }

    /**
     * This Method puts a game object at the specified position
     * @param gameObject the game object you wants to put
     * @param p the position you wants to put the game objects at
     * @return a boolean value, true -> assign success, false -> assign failure
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) { //Overload
        return p != null && putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
    }

    /**
     * This Method checks whether the point is out of bounds
     * @param x row
     * @param y col
     * @return a boolean value, true -> the point is out of bounds, false -> the point is in bounds
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * This Method checks whether the point is out of bounds
     * @param p the point you wannts to check
     * @return a boolean value, true -> the point is out of bounds, false -> the point is in bounds
     */
    public boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    /**
     * @return The char symbol of a list of game objects
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (GameObject[] gameObject : gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * @return Iterator of GameObject
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    /**
     * This class implements the Iterator of GameObject
     */
    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        /**
         * @return a boolean value, true -> the iterator has next value; false -> the iterator do not have the next value
         */
        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        /**
         * @return Game Object (next index)
         */
        @Override
        public GameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}