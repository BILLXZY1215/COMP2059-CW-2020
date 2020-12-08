/**
 * @author Zeyu Xiong
 */

package com.ae2dms;

public class GameRank implements Cloneable{
    private String Time;
    private int Id;
    private String MapSetName;
    private int MovesCount;
    public GameRank(String Time,  int Id, String MapSetName, int MovesCount){
        this.Time = Time;
        this.Id = Id;
        this.MapSetName = MapSetName;
        this.MovesCount = MovesCount;
    }

    /**
     * This Method returns the time you completed the game.
     * @return The time you completed the game.
     * For example: "19/11/2020 17:34:03"
     */
    public String getTime() {
        return Time;
    }

    /**
     * This Method set the time you completed the game.
     * @param time The time you completed the game.
     */
    public void setTime(String time) {
        Time = time;
    }


    /**
     * This Method get the id (ordered as a primary key in the table)
     * id = n means this is the nth completed game (ordered by completed time).
     * @return Id.
     */
    public int getId() {
        return Id;
    }


    /**
     * This Method set the MapSetName
     * @return MapSetName
     */
    public String getMapSetName() {
        return MapSetName;
    }


    /**
     * This Method returns the player's total number of moves
     * @return MovesCount
     */
    public int getMovesCount() {
        return MovesCount;
    }


    /**
     * This Method clones an object of GameRank.
     * @return The cloned object of GameRank
     */
    @Override
    public Object clone() {
        GameRank gameRank = null;
        try{
            gameRank = (GameRank) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return gameRank;
    }
}
