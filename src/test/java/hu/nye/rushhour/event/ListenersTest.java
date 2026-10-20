package hu.nye.rushhour.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.nye.rushhour.event.listener.AuditLoggerListener;
import hu.nye.rushhour.event.listener.MoveCounter;
import org.junit.jupiter.api.Test;

class ListenersTest {

    @Test
    void testMoveCounterShouldCountAppliedAndRejectedMoves() {
        MoveCounter underTest = new MoveCounter();
        underTest.onEvent(new GameEvent(GameEventType.MOVE_APPLIED, "a"));
        underTest.onEvent(new GameEvent(GameEventType.MOVE_APPLIED, "b"));
        underTest.onEvent(new GameEvent(GameEventType.MOVE_REJECTED, "c"));
        underTest.onEvent(new GameEvent(GameEventType.GAME_WON, "d"));
        assertEquals(2, underTest.getAppliedMoves());
        assertEquals(1, underTest.getRejectedMoves());
    }

    @Test
    void testAuditLoggerShouldHandleEveryEventType() {
        AuditLoggerListener underTest = new AuditLoggerListener();
        for (GameEventType type : GameEventType.values()) {
            assertDoesNotThrow(() -> underTest.onEvent(new GameEvent(type, "test")));
        }
    }
}
