package hu.nye.rushhour.event.listener;

import hu.nye.rushhour.event.GameEvent;
import hu.nye.rushhour.event.GameEventListener;
import hu.nye.rushhour.event.GameEventType;

/**
 * Counts the applied and the rejected moves of the game.
 */
public class MoveCounter implements GameEventListener {

    private int appliedMoves;
    private int rejectedMoves;

    @Override
    public void onEvent(GameEvent event) {
        if (event.type() == GameEventType.MOVE_APPLIED) {
            appliedMoves++;
        } else if (event.type() == GameEventType.MOVE_REJECTED) {
            rejectedMoves++;
        }
    }

    public int getAppliedMoves() {
        return appliedMoves;
    }

    public int getRejectedMoves() {
        return rejectedMoves;
    }
}
