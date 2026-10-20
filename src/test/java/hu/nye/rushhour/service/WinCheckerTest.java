package hu.nye.rushhour.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hu.nye.rushhour.TestBoards;
import hu.nye.rushhour.model.PlayerType;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class WinCheckerTest {

    private final WinChecker underTest = new WinChecker();

    @Test
    void testWinnerShouldBeEmptyWhenNoHeroReachedItsExit() {
        assertTrue(underTest.winner(TestBoards.of(".AA...", "..BB..")).isEmpty());
    }

    @Test
    void testWinnerShouldBeHumanWhenHeroAReachesRightEdge() {
        assertEquals(Optional.of(PlayerType.HUMAN), underTest.winner(TestBoards.of("....AA", "..BB..")));
    }

    @Test
    void testWinnerShouldBeAiWhenHeroBReachesLeftEdge() {
        assertEquals(Optional.of(PlayerType.AI), underTest.winner(TestBoards.of(".AA...", "BB....")));
    }
}
