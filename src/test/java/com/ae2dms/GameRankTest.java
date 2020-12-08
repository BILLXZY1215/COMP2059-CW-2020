package com.ae2dms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameRankTest {

    GameRank gameRank;

    /**
     * Time: 19/11/2020 17:34:03
     * Id: 1
     * MapSetName: Example Game!
     * MovesCount: 1279
     */
    @BeforeEach
    void setUp() {
        gameRank = new GameRank("19/11/2020 17:34:03",1,"Example Game!",1279);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTime() {
        Assertions.assertEquals("19/11/2020 17:34:03", gameRank.getTime());
    }

    @Test
    void setTime() {
        gameRank.setTime("29/11/2020 20:20:00");
        Assertions.assertEquals("29/11/2020 20:20:00", gameRank.getTime());
    }

    @Test
    void getId() {
        Assertions.assertEquals(1, gameRank.getId());
    }

    @Test
    void getMapSetName() {
        Assertions.assertEquals("Example Game!", gameRank.getMapSetName());
    }

    @Test
    void getMovesCount() {
        Assertions.assertEquals(1279, gameRank.getMovesCount());
    }
}