package com.ae2dms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GameObjectTest {


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', 'W', 'C', 'D', 'S', 'O', '=', 'G', 'L', 'P'})
    void fromChar(char c) {
        switch(c){
            case ' ':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.FLOOR);
                break;
            case 'W':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.WALL);
                break;
            case 'C':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.CRATE);
                break;
            case 'D':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.DIAMOND);
                break;
            case 'S':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.KEEPER);
                break;
            case 'O':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.CRATE_ON_DIAMOND);
                break;
            case '=':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.DEBUG_OBJECT);
                break;
            case 'G':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.GATE);
                break;
            case 'L':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.LOCK);
                break;
            case 'P':
                Assertions.assertEquals(GameObject.fromChar(c),GameObject.POWER);
                break;

        }
    }

    @Test
    void getCharSymbol() {
        GameObject obj = GameObject.FLOOR;
        Assertions.assertEquals(obj.getCharSymbol(),' ');
        obj = GameObject.WALL;
        Assertions.assertEquals(obj.getCharSymbol(),'W');
        obj = GameObject.CRATE;
        Assertions.assertEquals(obj.getCharSymbol(),'C');
        obj = GameObject.DIAMOND;
        Assertions.assertEquals(obj.getCharSymbol(),'D');
        obj = GameObject.KEEPER;
        Assertions.assertEquals(obj.getCharSymbol(),'S');
        obj = GameObject.CRATE_ON_DIAMOND;
        Assertions.assertEquals(obj.getCharSymbol(),'O');
        obj = GameObject.DEBUG_OBJECT;
        Assertions.assertEquals(obj.getCharSymbol(),'=');
        obj = GameObject.GATE;
        Assertions.assertEquals(obj.getCharSymbol(),'G');
        obj = GameObject.LOCK;
        Assertions.assertEquals(obj.getCharSymbol(),'L');
        obj = GameObject.POWER;
        Assertions.assertEquals(obj.getCharSymbol(),'P');
    }

}