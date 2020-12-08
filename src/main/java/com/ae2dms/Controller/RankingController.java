package com.ae2dms.Controller;

import com.ae2dms.GameEngine;
import com.ae2dms.GameRank;
import com.ae2dms.Starter.GameStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RankingController {

    private Stage RankingStage;
    private List<GameRank> gameRanks;

    @FXML
    private Button confirm;

    @FXML
    private VBox five;

    @FXML
    private Text rank5;

    @FXML
    private VBox four;

    @FXML
    private Text rank4;

    @FXML
    private VBox three;

    @FXML
    private Text rank3;

    @FXML
    private VBox two;

    @FXML
    private Text rank2;

    @FXML
    private VBox one;

    @FXML
    private Text rank1;


    /**
     * This Method will be triggered after clicking the "Confirm" button
     * @param event event
     * @throws Exception because showWindow may throw Exception
     */
    @FXML
    void confirm(ActionEvent event) throws Exception {
        GameStart gameStart = new GameStart(); //Design Pattern : Singleton
        gameStart.showWindow(RankingStage);
    }


    /**
     * This Method initializes the ranking controller
     * @param RankingStage the stage of the game
     * @throws IOException because RankingLoad may throw Exception
     */
    public void init(Stage RankingStage) throws IOException {
        this.RankingStage = RankingStage;
        RankingLoad();
        RankingSort(gameRanks);
        RankingDisplay(gameRanks);
    }


    /**
     * This Method gets the total number of records in ranking set
     * @return the total number of records in ranking set
     * @throws IOException FileInputStream
     */
    private int getRankingCount() throws IOException {
        File rankingCountFile = new File((System.getProperty("user.dir")+"/src/main/resources/ranking/ranking_count.skb"));
        InputStream currentRankingCount = new FileInputStream(rankingCountFile);
//        InputStream currentRankingCount = getClass().getClassLoader().getResourceAsStream("ranking/ranking_count.skb");
        BufferedReader readerCount = new BufferedReader(new InputStreamReader(currentRankingCount));

        while (true) {
            String line = readerCount.readLine();
            if(line.contains("Count")){
                return Integer.parseInt(line.replace("Count: ",""));
            }
        }
    }


    /**
     * This Method loads a ranking file,
     * and gets the basic information
     * @throws IOException FileInputStream
     */
    private void RankingLoad() throws IOException {
        File rankingCountFile = new File((System.getProperty("user.dir")+"/src/main/resources/ranking/ranking.skb"));
        InputStream Ranking = new FileInputStream(rankingCountFile);
//        InputStream Ranking = getClass().getClassLoader().getResourceAsStream("ranking/ranking.skb");
        BufferedReader reader = new BufferedReader(new InputStreamReader(Ranking));

        int count = getRankingCount();
        String Time = null, MapSetName = null;
        int Id = 0, MovesCount = 0;
        List<GameRank>  ranking = new ArrayList<>(count);
        while(true){
            String line = reader.readLine();
            if(line == null){
                break;
            }
            if(line.contains("Id")){
                Id = Integer.parseInt(line.replace("Id: ",""));
                continue;
            }
            if(line.contains("MovesCount")){
                MovesCount = Integer.parseInt(line.replace("MovesCount: ",""));
                continue;
            }
            if(line.contains("Time")){
                Time = line.replace("Time: ","");
                continue;
            }
            if(line.contains("MapSetName")){
                MapSetName = line.replace("MapSetName: ","");
                continue;
            }
            if(line.contains("---")){
                GameRank gameRank = new GameRank(Time, Id, MapSetName, MovesCount);
                ranking.add(gameRank);
            }
        }
        this.gameRanks = ranking;
        if(GameEngine.isDebugActive()) {
            for (int i = 0; i < count; i++) {
                GameRank gameRank = gameRanks.get(i);
                System.out.println(
                    "Time: " + gameRank.getTime() + "\n" +
                    "MapSetName: " + gameRank.getMapSetName() + "\n" +
                    "Id: " + gameRank.getId() + "\n" +
                    "MovesCount: " + gameRank.getMovesCount() + "\n"
                );
            }
        }
    }


    /**
     * This Method uses the bubble sort to swap positions among a list of gameRanks
     * @param gameRanks the list of instances of GameRanks
     */
    private void RankingSort(List<GameRank> gameRanks) { //Idea: Bubble Sort
        int size = gameRanks.size();
        for (int i = 0; i < size-1; i++) {
            for(int j = 0; j < size-1; j++){
                GameRank rank1 = gameRanks.get(j);
                GameRank rank2 = gameRanks.get(j+1);
                if(rank1.getMovesCount() > rank2.getMovesCount()){
                    //Swap Rank1 and Rank2
                    Collections.swap(gameRanks,j,j+1);
                }
            }
        }

        if(GameEngine.isDebugActive()) {
            for (int i = 0; i < size; i++) {
                GameRank gameRank = gameRanks.get(i);
                System.out.println(
                    "----------sort----------" + "\n" +
                    "Time: " + gameRank.getTime() + "\n" +
                    "MapSetName: " + gameRank.getMapSetName() + "\n" +
                    "Id: " + gameRank.getId() + "\n" +
                    "MovesCount: " + gameRank.getMovesCount() + "\n"
                );
            }
        }
    }


    /**
     * This Method displays te ranking of top 5 players
     * @param gameRanks the list of instances of GameRanks
     */
    private void RankingDisplay(List<GameRank> gameRanks) {
        int size = gameRanks.size();
        for(int i = 0; i< size; i++){
            GameRank gameRank = gameRanks.get(i);
            String output =
                    "Time: " + gameRank.getTime() + "\n" +
                    "MovesCount: " + gameRank.getMovesCount();
            //Interface only shows top5
            if(i==0){
                this.rank1.setText(output);
            }
            if(i==1){
                this.rank2.setText(output);
            }
            if(i==2){
                this.rank3.setText(output);
            }
            if(i==3){
                this.rank4.setText(output);
            }
            if(i==4){
                this.rank5.setText(output);
            }
        }
    }


}

