package hu.nye.rushhour.event;

/**
 * Kinds of events that happen during a game.
 */
public enum GameEventType {
    GAME_STARTED,
    MOVE_APPLIED,
    MOVE_REJECTED,
    BOARD_LOADED,
    BOARD_SAVED,
    GAME_WON,
    ERROR
}
