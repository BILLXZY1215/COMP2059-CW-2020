/**
 * @author Zeyu Xiong
 */

package com.ae2dms;

/**
 * This enum initializes the character of GameObjects
 */
public enum GameObject {
    WALL('W'),
    FLOOR(' '),
    CRATE('C'),
    DIAMOND('D'),
    KEEPER('S'),
    CRATE_ON_DIAMOND('O'),
    DEBUG_OBJECT('='),
    GATE('G'),
    LOCK('L'),
    POWER('P');

    public final char symbol;
    GameObject(final char symbol) {
        this.symbol = symbol;
    }
    public static GameObject fromChar(char c) { //To Upper Case
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }
        return WALL;
    }

    /**
     * @return the char symbol of the Game Object
     */
    public char getCharSymbol() {
        return symbol;
    }

}