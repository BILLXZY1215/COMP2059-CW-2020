/**
 * @author Zeyu Xiong
 */
package com.ae2dms;

import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.io.*;

public class GameEngine {
    public static final String GAME_NAME = "SokobanFX";
    public static GameLogger logger;
    private MediaPlayer mediaPlayer;
    private int movesCount = 0;
    private int undoCount = 0;
    private int powerCount = 0;
    private String mapSetName;
    private String LevelName;
    private String backKey;
    private String previouskeeperTarget;
    private HashMap<Integer, String> BackMap = new HashMap<Integer, String>();
    private HashMap <Integer, Integer> gatePositions = new HashMap<Integer, Integer>();
    private static boolean debug = true;
    private static boolean undo = false;
    private static boolean reset = true;
    private static boolean power = false;
    private Level currentLevel = null;
    private List<Level> levels;
    private boolean gameComplete = false;

    /**
     * This constructor inistializes the game, gets the loadfile and instance of game logger
     * @param input The inputStream of the file
     * @param production true -> getNextLevel; false -> do not getNextLevel
     */
    public GameEngine(InputStream input, boolean production) {
        try {
            logger = GameLogger.getInstance();
            levels = loadGameFile(input);
            if(production){
                currentLevel = getNextLevel();
            }
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + e.getStackTrace());
        }
    }


    /**
     * This Method adds (backKey, previousKeeperTarget) to a HashMap
     * @param backKey The operation when undo happens (UP/DOWN/RIGHT/LEFT)
     * @param previouskeeperTarget (Last Step) The Keeper's target
     */
    public void addHash(String backKey, String previouskeeperTarget) {
        undoCount++;
        if(previouskeeperTarget != "WALL") {
            BackMap.put(getUndoCount(), backKey+","+previouskeeperTarget); // add to HashMap
        }
        if(isDebugActive()){
            System.out.println("\n" + BackMap + "\n");
            String backkeyPreviouskeeperTarget = BackMap.get(getUndoCount());
            String[] split = backkeyPreviouskeeperTarget.split(",");
            String back = split[0];
            String previousTarget = split[1];

            System.out.println("backKey:"+ back + "\n" + "previousKeeperTarget:" + previousTarget);
        }
    }


    /**
     * This Method deletes (backKey, previousKeeperTarget) of a HashMap
     */
    public void deleteStep() {
        if(isDebugActive()){
            System.out.println("------Delete------\n");
            System.out.println("\n" + BackMap + "\n");
            String backkeyPreviouskeeperTarget = BackMap.get(getUndoCount());
            String[] split = backkeyPreviouskeeperTarget.split(",");
            String back = split[0];
            String previousTarget = split[1];

            System.out.println("backKey:"+ back + "\n" + "previousKeeperTarget:" + previousTarget);
        }
        BackMap.remove(getUndoCount());
        undoCount--;
        if(BackMap.isEmpty() && undoCount==0){
            undo = false; // Back to origin point, cannot undo
        }
    }

    /**
     * This Method makes keeper move by delta distances
     * @param delta the distance that keeper wants to move
     * @return a boolean value that indicates whether the keeper is moved or not
     * If the keeper's target is FLOOR, then swap the position of the keeper and FLOOR,
     * If the keeper's target is CRATE, and the CRATE's target is FLOOR, then swap the positions all of them,
     * (keeper -> CRATE_Position, CRATE -> FLOOR_Position, FLOOR -> keeper_Position).
     */
    public boolean move(Point delta) {
        if (isGameComplete()) {
            return false;
        }

        undo = true; //enable undo

        //Position of keeper
        Point keeperPosition = currentLevel.getKeeperPosition();
        //GameObject of keeper
        GameObject keeper = currentLevel.objectsGrid.getGameObjectAt(keeperPosition);
        //Position of 'keeper's target'
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        if(currentLevel.objectsGrid.isPointOutOfBounds(targetObjectPoint)){
            return false;
        }
        //GameObject of 'keeper's target'
        GameObject keeperTarget = currentLevel.objectsGrid.getGameObjectAt(targetObjectPoint);
        //Position of 'keeperTarget's target'
        Point keeperTargetObjectPoint = GameGrid.translatePoint(targetObjectPoint, delta);
        if(currentLevel.objectsGrid.isPointOutOfBounds(keeperTargetObjectPoint)){
            return false;
        }
        //GameObject of 'keeperTarget's target'
        GameObject keeperTargetTarget = currentLevel.objectsGrid.getGameObjectAt(keeperTargetObjectPoint);

        boolean keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                if(power){
                    if(powerCount - 3 < 0){
                        break;
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Blow Up");
                    alert.setHeaderText("Blow Up the Wall");
                    alert.setContentText("Are you sure to Blow up this wall? (consume 3 points of power) ");
                    javafx.scene.control.Button btn = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                    btn.setId("OK");
                    Optional result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        currentLevel.objectsGrid.removeGameObjectAt(targetObjectPoint); // BLOW UP WALL
                        currentLevel.objectsGrid.putGameObjectAt(GameObject.FLOOR, targetObjectPoint); // Replace it with FLOOR
                        powerCount-=3;
                        if(powerCount <= 0){
                            power = false;// A power can only blow up 3 grids of wall
                            powerCount = 0; //reset
                        }
                    }
                }
                break;

            case CRATE:
                this.previouskeeperTarget = "CRATE";
                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) { // crate cannot move
                    break;
                }else if (currentLevel.diamondsGrid.getGameObjectAt(keeperTargetObjectPoint) == GameObject.GATE){
                    break; // crate cannot move to gate
                }else if (currentLevel.diamondsGrid.getGameObjectAt(keeperTargetObjectPoint) == GameObject.POWER ){
                    break; // crate cannot move to power
                }
                // swap position : [keeper, CRATE, FLOOR] -> [FLOOR, keeper, CRATE]
                // CRATE stands for: keeperTarget
                // FLOOR stands for: keeperTargetTarget
                currentLevel.objectsGrid.putGameObjectAt(keeperTargetTarget, keeperPosition); // FLOOR -> keeperPosition
                currentLevel.objectsGrid.putGameObjectAt(keeper, targetObjectPoint); // keeper -> targetObjectPoint
                currentLevel.objectsGrid.putGameObjectAt(keeperTarget, keeperTargetObjectPoint); // keeperTarget -> keeperTargetObjectPoint
                keeperMoved = true;
                break;

            case LOCK:
                if(power){
                    // unlock!
                    currentLevel.objectsGrid.removeGameObjectAt(targetObjectPoint); // REMOVE LOCK
                    currentLevel.objectsGrid.putGameObjectAt(GameObject.FLOOR, targetObjectPoint); // Replace it with FLOOR
                    powerCount--;
                    if(powerCount <= 0){
                        power = false;// A power can only unlock 3 ways
                        powerCount = 0; //reset
                    }
                }else{
                    // the keeper has no power to unlock it!
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Locked");
                    alert.setHeaderText("The way has been locked!");
                    alert.setContentText("You need to get power (two-headed sunflower or ballons) first!");
                    alert.showAndWait();
                }
                break;

            case FLOOR:
                if(currentLevel.diamondsGrid.getGameObjectAt(targetObjectPoint) == GameObject.GATE){ // Target is a gate
                    gatePositions = currentLevel.getGatePositions(); // HashMap Activated
                    int GateX = (int) targetObjectPoint.getX();
                    int GateY = (int) targetObjectPoint.getY();
                    for(Integer i : gatePositions.keySet()){
                        if(GateX == i && GateY == gatePositions.get(i)){ // Get the current Gate Position
                            continue;
                        }else{
                            this.previouskeeperTarget = "GATE";
                            //Get the target Gate Position
                            targetObjectPoint = new Point(i, gatePositions.get(i));
                            currentLevel.objectsGrid.putGameObjectAt(GameObject.FLOOR, keeperPosition); // the previous position of keeper becomes floor
                            currentLevel.objectsGrid.putGameObjectAt(keeper, targetObjectPoint); //send keeper to the other gate
                            break;
                        }
                    }
                }else if(currentLevel.diamondsGrid.getGameObjectAt(targetObjectPoint) == GameObject.POWER){ // Target is a Power
                    currentLevel.diamondsGrid.removeGameObjectAt(targetObjectPoint); // REMOVE Power
                    currentLevel.objectsGrid.putGameObjectAt(keeper, targetObjectPoint); // Replace it with keeper
                    currentLevel.objectsGrid.putGameObjectAt(GameObject.FLOOR, keeperPosition); // the previous position of keeper becomes floor
                    this.power = true; // keeper has got the power!
                    powerCount += 3; // Each Power has 3 chances to unlock
                }else{
                    this.previouskeeperTarget = "FLOOR";
                    // swap position of FLOOR and Keeper
                    currentLevel.objectsGrid.putGameObjectAt(keeperTarget, keeperPosition);
                    currentLevel.objectsGrid.putGameObjectAt(keeper, targetObjectPoint);
                }
                keeperMoved = true;
                break;

            default:
                logger.severe("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }

        if (keeperMoved) {
            addHash(getBackKey(), getPreviouskeeperTarget());
            keeperPosition.setLocation(targetObjectPoint);
            movesCount++;
            if (currentLevel.isComplete()) {
                if (isDebugActive()) {
                    System.out.println("\nLevel complete!\n");
                }
                currentLevel = getNextLevel();
            }
        }

        //For Debug
        if(!isGameComplete()){
            String name = currentLevel.getName();
            int index = currentLevel.getIndex();
            if (GameEngine.isDebugActive()) {
                System.out.println("\n");
                System.out.println("Current level state:"+ "[" + index + "]");
                System.out.println("\n "+ name );
                System.out.println(currentLevel.ObjectToString());
                System.out.println("Keeper pos: " + keeperPosition);
                System.out.println("Movement source obj: " + keeper);
                System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
                System.out.println("\n-------------------------\n");
            }
        }
        return keeperMoved;
    }

    /**
     * This Method undos the move()
     * @param delta The distance from the previous state moves to the current state
     * @param deltaback The opposite distance of delta
     * @param previouskeeperTarget The previous keeper target ("CRATE"/"FLOOR")
     */
    private void moveBack(Point delta, Point deltaback, String previouskeeperTarget){
        //Position of keeper
        Point keeperPosition = currentLevel.getKeeperPosition();
        //GameObject of keeper
        GameObject keeper = currentLevel.objectsGrid.getGameObjectAt(keeperPosition);
        //Position of 'keeper's target' -> FLOOR
        Point FLOOR = GameGrid.translatePoint(keeperPosition, delta);
        //GameObject of 'keeper's target' -> FLOOR
        GameObject FLOORObj = currentLevel.objectsGrid.getGameObjectAt(FLOOR);

        boolean keeperMoved = false;

        switch(previouskeeperTarget){
            case "CRATE":
                //Position of 'keeper's target' -> CRATE
                Point CRATE = GameGrid.translatePoint(keeperPosition, deltaback);
                //GameObject of 'keeper's target' -> CRATE
                GameObject CRATEObj = currentLevel.objectsGrid.getGameObjectAt(CRATE);

                //swap back: [FLOOR, Keeper, CRATE] -> [Keeper, CRATE, FLOOR]
                currentLevel.objectsGrid.putGameObjectAt(CRATEObj, keeperPosition); // CRATE -> KeeperPosition
                currentLevel.objectsGrid.putGameObjectAt(keeper, FLOOR); //Keeper -> FLOOR
                currentLevel.objectsGrid.putGameObjectAt(FLOORObj, CRATE); //FLOORObj -> CRATE
                keeperMoved = true;
                break;

            case "FLOOR":
                //swap back of KEEPER and FLOOR
                currentLevel.objectsGrid.putGameObjectAt(FLOORObj, keeperPosition);
                currentLevel.objectsGrid.putGameObjectAt(keeper, FLOOR);
                keeperMoved = true;
                break;

            case "GATE":
                int GateX = (int) keeperPosition.getX();
                int GateY = (int) keeperPosition.getY();
                for(Integer i : gatePositions.keySet()){
                    if(GateX == i && GateY == gatePositions.get(i)){ // Get the current Gate Position
                        continue;
                    }else{
                        //Get the target Gate Position
                        Point targetObjectPoint = new Point(i, gatePositions.get(i)); // get the other gate' position
                        targetObjectPoint.translate((int) delta.getX(), (int) delta.getY()); //get the previous position of the keeper
                        currentLevel.objectsGrid.putGameObjectAt(GameObject.FLOOR, keeperPosition); // the previous position of keeper becomes floor
                        currentLevel.objectsGrid.putGameObjectAt(keeper, targetObjectPoint); //send keeper to the other gate
                        keeperPosition.setLocation(targetObjectPoint); // update keeperPosition
                        break;
                    }
                }
                movesCount++; //back still counts a move
                break;

            default:
                logger.severe("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }
        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            movesCount++; //back still counts a move
        }



    }


    /**
     * This method firstly gets the (backKey, previousKeeperTarget) from BackMap(The HashMap),
     * then calls moveBack() to swap position back to the previous step
     */
    public void undo() {
        String[] backkeyPreviouskeeperTarget = BackMap.get(getUndoCount()).split(",");
        String backKey = backkeyPreviouskeeperTarget[0];
        String previousKeeperTarget = backkeyPreviouskeeperTarget[1];

        System.out.println("backready:"+ backKey + "\n" + "targetready:" + previousKeeperTarget);

        switch(backKey){
            case "DOWN":
                moveBack(new Point(1,0), new Point(-1,0), previousKeeperTarget);
                deleteStep();
                break;
            case "UP":
                moveBack(new Point(-1,0), new Point(1,0), previousKeeperTarget);
                deleteStep();
                break;
            case "LEFT":
                moveBack(new Point(0,-1), new Point(0,1), previousKeeperTarget);
                deleteStep();
                break;
            case "RIGHT":
                moveBack(new Point(0,1), new Point(0,-1), previousKeeperTarget);
                deleteStep();
                break;
            default:
                break;
        }
        if (GameEngine.isDebugActive()) {
            Point keeperPosition = currentLevel.getKeeperPosition();
            System.out.println("\n----------undo----------");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
        }

    }

    /**
     * This Method is called from MenuController.resetLevel(),
     * assign the return value of resetLevel() to currentLevel
     */
    public void getResetLevel() {
        reset = false; //only can reset once
        undo = false; //Disable undo
        BackMap.clear(); //clear Step History
        undoCount = 0; //clear undoCount
        power = false; // disable power
        powerCount = 0; //reset powerCount
        currentLevel = resetLevel();
        if (GameEngine.isDebugActive()) {
            Point keeperPosition = currentLevel.getKeeperPosition();
            System.out.println("\n----------reset----------");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
        }
    }


    /**
     * This Method assigns the objectsGrid and diamondsGrid of default current level to currentLevel
     * @return current level
     */
    public Level resetLevel(){
        Point defaultkeeper = currentLevel.getDefaultKeeperPosition();
        currentLevel.setKeeperPosition(defaultkeeper);
        currentLevel.objectsGrid = currentLevel.DefaultObjectsGrid;
        currentLevel.diamondsGrid = currentLevel.DefaultDiamondsGrid;
        return currentLevel;
    }


    /**
     * This Method loads game file line by line,
     * then assign value for CurrentLevel, movesCount, MapSetName and LevelName
     * @param input The inputStream of the file
     * @return list of levels (each level is a Level object)
     */
    private List<Level> loadGameFile(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            boolean parsedFirstLevel = false;
            int currentLevelIndex = -1;
            List<String> rawLevel = new ArrayList<>();

            while (true) {
                String line = reader.readLine();
                if (line == null) { //Add the final Level
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(LevelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    if(currentLevelIndex > 0){ //has modified
                        currentLevel = levels.get(currentLevelIndex-1);
                    }
                    break;
                }
                if (line.contains("CurrentLevel")) {
                    currentLevelIndex = Integer.parseInt(line.replace("CurrentLevel: ",""));
                    continue;
                }
                if (line.contains("movesCount")) {
                    movesCount = Integer.parseInt(line.replace("movesCount: ", ""));
                    continue;
                }
                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }
                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) { // Avoid pass an empty String ArrayList 'rawlevel' into class Level
                        Level parsedLevel = new Level(LevelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }
                    LevelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                if (line.matches(".*W.*W.*")) { // has walls
                    rawLevel.add(line);
                }
            }

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    /**
     * @return true -> game completed; false -> game not completed
     */
    public boolean isGameComplete() {
        return gameComplete;
    }


    /**
     * This Method gets the level (next index) in List<Level>
     * @return the next level (next index)
     */
    public Level getNextLevel() {
        power = false; //Disable Power
        powerCount = 0; //reset powerCount
        undo = false; //Disable undo;
        reset = true; //Enable reset;
        BackMap.clear(); //clear Step History
        undoCount = 0; //clear undoCount
        if (currentLevel == null) {
            return levels.get(0);
        }
        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex); //Should not +1 -> index should automatically -1
        }
        gameComplete = true; // (currentLevelIndex = levels.size()) --> Last Level has completed!
        return null;
    }

    /**
     * @return current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * switch debug's status
     */
    public void toggleDebug() {
        debug = !debug;
    }

    /**
     * @return debug, true -> debug is activated; false -> debug is not activated
     */
    public static boolean isDebugActive() {
        return debug;
    }

    /**
     * @return undo, true -> undo is activated; false -> undo is not activated
     */
    public static boolean isUndoActive() {
        return undo;
    }

    /**
     * @return reset, true -> reset is activated; false -> reset is not activated
     */
    public static boolean isResetActive() {
        return reset;
    }

    /**
     * @return power, true -> power is activated; false -> power is not activated
     */
    public static boolean isPower() {
        return power;
    }

    /**
     * @return the name of the mapSet
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * @return List<Level>
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * @return moves count
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * @return undo count
     */
    public int getUndoCount() {return undoCount;}

    /**
     * @return back Key
     */
    public String getBackKey() {return backKey;}

    /**
     * @param backKey set back Key
     */
    public void setBackKey(String backKey) {this.backKey = backKey;}

    /**
     * @return previous keeper target
     */
    public String getPreviouskeeperTarget() {return previouskeeperTarget;}

    /**
     * @return the number of powers left
     */
    public int getPowerCount() {
        return powerCount;
    }

    /**
     * @return the Back HashMap for Undo
     */
    public HashMap<Integer, String> getBackMap () {
        return BackMap;
    }
}