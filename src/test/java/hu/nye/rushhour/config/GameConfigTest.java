package hu.nye.rushhour.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class GameConfigTest {

    @Test
    void testGetInstanceShouldAlwaysReturnTheSameObject() {
        assertSame(GameConfig.getInstance(), GameConfig.getInstance());
    }

    @Test
    void testGetInstanceShouldProvideTheDefaultSettings() {
        GameConfig config = GameConfig.getInstance();
        assertEquals("board.txt", config.getDefaultBoardFile());
        assertEquals("savegame.txt", config.getDefaultSaveFile());
        assertEquals(10, config.getHighScoreLimit());
        assertEquals("Computer", config.getComputerName());
        assertEquals("jdbc:h2:./rushhour-db", config.getDatabaseUrl());
    }
}
