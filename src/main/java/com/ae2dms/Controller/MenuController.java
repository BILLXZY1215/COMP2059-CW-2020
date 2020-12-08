/**
 * @author Zeyu Xiong
 */
package com.ae2dms.Controller;
import com.ae2dms.*;
import com.ae2dms.GraphicObject.GraphicObjectFactory;
import com.ae2dms.Observer.LevelStepObserver;
import com.ae2dms.Observer.PowerObserver;
import com.ae2dms.Observer.Subject;
import com.ae2dms.Starter.GameStart;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MenuController {
    private Stage primaryStage;
    private GameEngine gameEngine;
    private GridPane gameGrid;
    private static boolean music = true;
    private int mode = 0;
    private MediaPlayer mediaPlayer;

    //Observer Design Pattern
    Subject subject = new Subject();
    LevelStepObserver levelStepObserver = new LevelStepObserver(subject);
    PowerObserver powerObserver = new PowerObserver(subject);


    @FXML
    private MenuItem SaveGame;

    @FXML
    private MenuItem LoadGame;

    @FXML
    private MenuItem Exit;

    @FXML
    private MenuItem Undo;

    @FXML
    private RadioMenuItem Music;

    @FXML
    private RadioMenuItem Debug;

    @FXML
    private MenuItem ResetLevel;

    @FXML
    private MenuItem Plant;

    @FXML
    private MenuItem Zombie;

    @FXML
    private MenuItem About;

    @FXML
    private Text levelText;

    @FXML
    private Text stepText;

    @FXML
    private Text powerText;


    /**
     * This Method initializes the music, debug, Stage and GameGrid,
     * and then load the default game file,
     * finally update level and movesCount
     * @param primaryStage the stage of the game
     * @param gameGrid the grids of the map
     * @throws Exception because loadDefaultSaveFile may throw the exception
     */
    public void init(Stage primaryStage, GridPane gameGrid) throws Exception {
        this.Undo.setDisable(true);
        this.Music.setSelected(true);
        music = true;
        playMusic();
        this.Debug.setSelected(true);
        this.primaryStage = primaryStage;
        this.gameGrid = gameGrid;
        loadDefaultSaveFile(primaryStage);
        level_step_fresh();
    }

    /**
     * This Method initializes the music, debug, Stage and GameGrid,
     * and then load the selected game file
     * @param primaryStage the stage of the game
     * @param gameGrid the grids of the map
     * @throws Exception because loadDefaultSaveFile may throw the exception
     */
    public void init(Stage primaryStage, GridPane gameGrid, InputStream input) throws Exception {
        this.Undo.setDisable(true);
        this.Music.setSelected(true);
        playMusic();
        this.Debug.setSelected(true);
        this.primaryStage = primaryStage;
        this.gameGrid = gameGrid;
        initializeGame(input,true); // LoadGame from default page
        setEventFilter();
        level_step_fresh();
    }

    /**
     * This Method shows "About Message"
     * @param event event
     */
    @FXML
    void About(ActionEvent event) {
        showAbout();
    }


    /**
     * This Method toggles debug
     * @param event event
     * @throws Exception because toggleDebug may throw exception
     */
    @FXML
    void Debug(ActionEvent event) throws Exception {
        toggleDebug();
    }

    /**
     * This Method firstly shows a dialog and asks if you indeed wants to exit the game,
     * if so, then exit the game,
     * otherwise close the dialog, game continue
     * @param event event
     */
    @FXML
    void Exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure to exit?");
        javafx.scene.control.Button btn = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        btn.setId("CANCEL");
        Optional result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            closeGame();
        }
    }


    /**
     * This Method Load a game，
     * And activates undo and reset
     * @param event event
     */
    @FXML
    void LoadGame(ActionEvent event) {
        loadGame();
        this.Undo.setDisable(true);
        this.ResetLevel.setDisable(true);
    }


    /**
     * This Method toggles music
     * @param event event
     */
    @FXML
    void Music(ActionEvent event) {
        toggleMusic();
    }


    /**
     * This Method reset the level,
     * then disable undo and reset
     * @param event event
     * @throws Exception because reset may throw exception
     */
    @FXML
    void Reset(ActionEvent event) throws Exception {
        resetLevel();
        undoFresh();
        resetFresh();
    }

    /**
     * This Method saves a game
     * @param event event
     */
    @FXML
    void SaveGame(ActionEvent event) {
        saveGame();
    }


    /**
     * This Method undo for a step,
     * then disable undo,
     * finally update level and movesCount
     * @param event event
     * @throws Exception because undo may throw exception
     */
    @FXML
    void Undo(ActionEvent event) throws Exception {
        undo();
        undoFresh();
        level_step_fresh();
    }


    /**
     * Switch to Plant Mode
     * @param event event
     * @throws Exception because reloadGird may throw exception
     */
    @FXML
    void Plant(ActionEvent event) throws Exception {
        mode = 0;
        reloadGrid();
    }

    /**
     * Switch to Zombie Mode
     * @param event event
     * @throws Exception because reloadGird may throw exception
     */
    @FXML
    void Zombie(ActionEvent event) throws Exception {
        mode = 1;
        reloadGrid();
    }


    /**
     * This Method detects the click of the keyboard
     * @param code the code from keyboard
     * @return a boolean value indicates the keeper has moved or not
     * @throws Exception because undo may throw exception
     * UP/W: move up, and save back key for undo
     * DOWN/S: move down, and save back key for undo
     * RIGHT/D: move right, and save back key for undo
     * LEFT/A: move left, and save back key for undo
     * B: undo FastKey
     * E: exit FastKey
     * M: music FastKey
     */
    public boolean handleKey(KeyCode code) throws Exception {
        if (GameEngine.isDebugActive()) {
            System.out.println(code);
        }
        boolean hasMoved = false;
        switch (code) {
            case UP:
            case W:
                gameEngine.setBackKey("DOWN");
                hasMoved = gameEngine.move(new Point(-1, 0));
                level_step_fresh();
                break;

            case RIGHT:
            case D:
                gameEngine.setBackKey("LEFT");
                hasMoved = gameEngine.move(new Point(0, 1));
                level_step_fresh();
                break;

            case DOWN:
            case S:
                gameEngine.setBackKey("UP");
                hasMoved = gameEngine.move(new Point(1, 0));
                level_step_fresh();
                break;

            case LEFT:
            case A:
                gameEngine.setBackKey("RIGHT");
                hasMoved = gameEngine.move(new Point(0, -1));
                level_step_fresh();
                break;

            case B: // Undo FastKey
                if(!this.Undo.isDisable()) {
                    undo();
                    undoFresh();
                    level_step_fresh();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Undo");
                    alert.setHeaderText("Undo");
                    alert.setContentText("You cannot Undo at this state! ");
                    alert.showAndWait();
                }
                break;

            case E: // Exit FastKey
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText("Exit");
                alert.setContentText("Are you sure to exit the game? ");
                Optional result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    closeGame();
                }

            case M: //Toggle Music FastKey
                toggleMusic();
                break;

            default:

        }
        return hasMoved;
    }


    /**
     * This Method updates:
     * level
     * steps (movesCount)
     * power
     * in the menu bar
     */
    public void level_step_fresh() {
        if(gameEngine.isGameComplete()){
            return;
        }
        subject.setStepState(gameEngine.getMovesCount());
        subject.setPowerState(gameEngine.getPowerCount());
        this.levelText.setText("Level: " + gameEngine.getCurrentLevel().getIndex());
        this.stepText.setText("Step: " + levelStepObserver.step);
        this.powerText.setText("Power: " + powerObserver.power);
    }


    /**
     * This Method change the status of undo
     * true -> false; false -> true
     */
    public void undoFresh(){
        this.Undo.setDisable(!GameEngine.isUndoActive());
    }

    /**
     * This Method change the status of reset
     * true -> false; false -> true
     */
    public void resetFresh(){
        this.ResetLevel.setDisable(!GameEngine.isResetActive());
    }


    /**
     * This Method load the default saved file ("SampleGame.skb")
     * @param primaryStage the stage of the game
     * @throws Exception because initializeGame may throw exception
     */
    void loadDefaultSaveFile(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        if(in == null){
            throw new IllegalArgumentException("Point cannot be null.");
        }
        initializeGame(in,true);
        setEventFilter();
    }

    /**
     * This Method load the default Debug file ("debugGame.skb")
     * @param primaryStage the stage of the game
     * @throws Exception because initializeGame may throw exception
     */
    void loadDefaultDebugFile(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        InputStream in = getClass().getClassLoader().getResourceAsStream("debugGame.skb");
        if(in == null){
            throw new IllegalArgumentException("Point cannot be null.");
        }
        initializeGame(in,true);
        setEventFilter();
    }


    /**
     * This Method initializes a game
     * @param input The inputStream of the file
     * @param production a boolean value, if true, the currentLevel in gameEngine needs to getNextLevel
     * @throws Exception
     */
    private void initializeGame(InputStream input, boolean production) throws Exception {
        gameEngine = new GameEngine(input, production);
        reloadGrid();
    }


    /**
     * This Method detects the input from the keyboard,
     * and passes it into the method handleKey() as a parameter,
     * finally try to reloadGrid
     */
    private void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(!gameEngine.isGameComplete()){
                try {
                    if(handleKey(event.getCode())){
                        undoFresh();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    reloadGrid();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * This Method load a selected game file,
     * get the input stream and passes it to initializeGame() as a parameter
     * @throws Exception because initializeGame may throw exception
     */
    private void loadGameFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban load file", "*.skb"));
        File loadFile = fileChooser.showOpenDialog(primaryStage);

        if (loadFile != null) {
            if (GameEngine.isDebugActive()) {
                GameEngine.logger.info("Load file: " + loadFile.getName());
            }
            InputStream input = new FileInputStream(loadFile);
            initializeGame(input,true); // The currentLevel in gameEngine do not need to getNextLevel
            level_step_fresh();
        }
    }


    /**
     * This Method saves a game file,
     * The basic idea is to get the correct format of information in each level,
     * includes MapSetName, movesCount, CurrentLevel (index), map of each level
     * @throws IOException it is related to read/write file
     */
    private void saveGameFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        File saveFile = fileChooser.showSaveDialog(primaryStage);
        if (saveFile != null) {
            FileOutputStream output = new FileOutputStream(saveFile);
            Level temp;
            List<Level> levels = gameEngine.getLevels();
            String mapSetName = "MapSetName: " + gameEngine.getMapSetName() + "\n";
            String movesCount = "movesCount: " + gameEngine.getMovesCount() + "\n";
            int currentLevelIndex = gameEngine.getCurrentLevel().getIndex()-1;
            String currentIndex = "CurrentLevel: " + currentLevelIndex + "\n";
            try {
                output.write(currentIndex.getBytes()); // Write CurrentLevel
                output.write(movesCount.getBytes()); // Write movesCount
                output.write(mapSetName.getBytes()); // Write mapSetName
                for (Level level : levels) {
                    temp = level;
                    String name = "LevelName: " + temp.getName() + "\n";
                    output.write(name.getBytes());
                    String gameMap = temp.CombineToString() + "\n";
                    output.write(gameMap.getBytes());
                }
                if (GameEngine.isDebugActive()) {
                    GameEngine.logger.info("Save file: " + saveFile.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            output.close(); //Close FileOutputStream
            output.flush();
        }
    }

    /**
     * This Method basically reload the map,
     * if game is completed, then it stops playing the music and goes back to the GameStart scene
     * @throws Exception because rankingUpdate and showWindow may throw the exception
     */
    private void reloadGrid() throws Exception {
        if (gameEngine.isGameComplete()) {
            this.mediaPlayer.pause();
            this.mediaPlayer = null;
//            toggleMusic(); //Stop Playing
            showVictoryMessage();
            rankingUpdate();
            GameStart gameStart = new GameStart();
            gameStart.showWindow(primaryStage); //Back to default page
            return;
        }

        Level currentLevel = gameEngine.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) { // Ergodic the whole map of a level
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition(), mode);
        }
        gameGrid.autosize();
        primaryStage.sizeToScene();
    }


    /**
     * This Method gets the total number of records in ranking set
     * @return the total number of records in ranking set
     * @throws IOException FileInputStream
     */
    private int getRankingCount() throws IOException {
//        File rankingCountFile = new File(String.valueOf(getClass().getClassLoader().getResourceAsStream("ranking_count.skb")));
        InputStream currentRankingCount = getClass().getClassLoader().getResourceAsStream("ranking/ranking_count.skb");
        BufferedReader readerCount = new BufferedReader(new InputStreamReader(currentRankingCount));

        while (true) {
            String line = readerCount.readLine();
            if(line.contains("Count")){
                return Integer.parseInt(line.replace("Count: ",""));
            }
        }
    }

    /**
     * This Method sets the total number of records in ranking set
     * @param count the total number of records in ranking set
     * @throws IOException FileInputStream
     */
    private void setRankingCount(int count) throws IOException {
//        File rankingCountFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("ranking/ranking_count.skb")).getFile());
//        File rankingCountFile = new File("src/main/resources/ranking/ranking_count.skb");
//        File rankingCountFile = new File("target/classes/ranking/ranking_count.skb");
        File rankingCountFile = new File((System.getProperty("user.dir")+"/src/main/resources/ranking/ranking_count.skb"));
        FileOutputStream output = new FileOutputStream(rankingCountFile);
        String out = "Count: " + count;
        output.write(out.getBytes());
//        output.close();
        output.flush();
    }


    /**
     * This Method updates the ranking,
     * write a file with the information of id, mapSetName, and movesCount
     * @throws IOException because setRankingCount may throw exception
     */
    private void rankingUpdate() throws IOException {
        int count = getRankingCount() + 1;
//        File rankingFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("ranking/ranking.skb")).getFile());
//        File rankingFile = new File("src/main/resources/ranking/ranking.skb");
//        File rankingFile = new File("target/classes/ranking/ranking.skb");
        File rankingFile = new File((System.getProperty("user.dir")+"/src/main/resources/ranking/ranking.skb"));
        String ranking =
                "Id: " + count + "\n" +
                "MapSetName: " + gameEngine.getMapSetName() + "\n"+
                "MovesCount: " + gameEngine.getMovesCount() + "\n"+
                "---" + "\n";
        String message = GameEngine.logger.createFormattedMessage(ranking);
        FileOutputStream output = new FileOutputStream(rankingFile,true);
        output.write(message.getBytes());
        setRankingCount(count); //Update Count Number
//        output.close();
        output.flush();
    }


    /**
     * This Method shows a dialog of victory,
     * you can see the MapSetName and the final moves count
     */
    private void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameEngine.getMapSetName() + "\n in " + gameEngine.getMovesCount() + " moves!";
        MotionBlur mb = new MotionBlur(1, 1);
        newDialog(dialogTitle, dialogMessage, mb);
        if (GameEngine.isDebugActive()) {
            GameEngine.logger.info(dialogMessage);
        }
    }


    /**
     * This Method defines a style of a dialog
     * @param dialogTitle The title of the dialog
     * @param dialogMessage The message of the dialog
     * @param dialogMessageEffect The style of the dialog
     */
    private void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setId("AboutText");
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font("Phosphate",20));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setId("AboutVBox");
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }


    /**
     * This Method add a gameObject to the gameGrid
     * @param gameObject the instance of GameObject
     * @param location the location of the gameObject
     * @param mode 0 -> Plant Mode, 1 -> Zombie Mode
     */
    private void addObjectToGrid(GameObject gameObject, Point location, int mode) {
        GraphicObjectFactory graphicObjectFactory = new GraphicObjectFactory();
        switch (gameObject) {
            case WALL:
                gameGrid.add(graphicObjectFactory.getWall(mode), location.y, location.x);
                break;

            case CRATE:
                gameGrid.add(graphicObjectFactory.getCrate(mode), location.y, location.x);
                break;

            case DIAMOND:
                gameGrid.add(graphicObjectFactory.getDiamond(mode), location.y, location.x);
                break;

            case KEEPER:
                gameGrid.add(graphicObjectFactory.getKeeper(mode), location.y, location.x);
                break;

            case FLOOR:
                gameGrid.add(graphicObjectFactory.getFloor(mode), location.y, location.x);
                break;

            case CRATE_ON_DIAMOND:
                gameGrid.add(graphicObjectFactory.getCrate_On_Diamond(mode), location.y, location.x);
                break;

            case GATE:
                gameGrid.add(graphicObjectFactory.getGate(mode), location.y, location.x);
                break;

            case LOCK:
                gameGrid.add(graphicObjectFactory.getLock(mode), location.y, location.x);
                break;

            case POWER:
                gameGrid.add(graphicObjectFactory.getPower(mode), location.y, location.x);
                break;


            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameEngine.logger.severe(message);
                throw new AssertionError(message);
        }
    }


    /**
     * This Method close and exit the game
     */
    public void closeGame() {
        System.exit(0);
    }

    /**
     * This Method calls saveGameFile() to save a game
     */
    public void saveGame() {
        try {
            saveGameFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method calls loadGameFile() to load a game
     */
    public void loadGame() {
        try {
            loadGameFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This Method calls newDialog with parameters of title and message
     */
    public void showAbout() {
        String title = "About this game";
        String message = "Game created by Zeyu XIONG\n";
        newDialog(title, message, null);
    }


    /**
     * This Method calls getResetLevel() in gameEngine
     * @throws Exception because reloadGrid may throw exception
     */
    public void resetLevel() throws Exception {
        gameEngine.getResetLevel();
        level_step_fresh();
        reloadGrid();
    }


    /**
     * This Method calls toggleDebug() in gameEngine
     * @throws Exception because reloadGrid may throw exception
     */
    public void toggleDebug() throws Exception {
        gameEngine.toggleDebug();
        reloadGrid();
    }

    /**
     * This Method calls undo in gameEngine
     * @throws Exception because reloadGrid may throw exception
     */
    public void undo() throws Exception {
        gameEngine.undo();
        reloadGrid();
    }

    /**
     * This Method returns the status of music
     * @return the status of music
     */
    public static boolean isMusicActive() {
        return music;
    }

    /**
     * This Method controls the play/stop of music
     */
    public void toggleMusic() {
        music = !music;
        if(isMusicActive()){
            this.mediaPlayer.play();
        }else{
            this.mediaPlayer.pause();
        }
    }

    /**
     * This Method is the core of playing music,
     * load the music file,
     * then create a media player,
     * and set to auto/loop play
     */
    public void playMusic() {
        if(isMusicActive()){
            // Create the media source
            String url;
            url = Objects.requireNonNull(this.getClass().getClassLoader().getResource("music/Faster.mp3")).toExternalForm();
            Media media =new Media(url);

            if (GameEngine.isDebugActive()) {
                // All info about the Audio Resources
                ObservableMap<String, Object> map = media.getMetadata();
                for (String key : map.keySet()) {
                    System.out.println(key + "-" + map);
                }
            }

            // Create the player and set to play automatically.
            this.mediaPlayer = new MediaPlayer(media);
            this.mediaPlayer.setAutoPlay(true);
            if(GameEngine.isDebugActive()){
                System.out.println("Music starts to Play!");
            }
            this.mediaPlayer.setOnEndOfMedia(() -> {
                if(GameEngine.isDebugActive()){
                    System.out.println("Music Stopped and loop again!");
                }
                // Loop Play
                this.mediaPlayer.seek(Duration.ZERO);
                this.mediaPlayer.play();
            });

            // Create the view and add it to the Scene.
            // MediaView mediaview =new MediaView(mediaPlayer);
        }
    }

    public MenuItem getUndo(){
        return Undo;
    }

    public RadioMenuItem getMusic(){
        return Music;
    }

    public RadioMenuItem getDebug(){
        return Debug;
    }

    public void setMusic(boolean value){
       music = value;
    }

    public void setGameGrid(GridPane gameGrid){
        this.gameGrid = gameGrid;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
