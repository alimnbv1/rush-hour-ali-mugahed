package hu.nye.rushhour.event;

import java.util.Objects;

/**
 * Immutable event object sent from the publisher to the listeners.
 *
 * @param type    kind of the event
 * @param message human readable details
 */
public record GameEvent(GameEventType type, String message) {

    /**
     * Validating constructor.
     */
    public GameEvent {
        Objects.requireNonNull(type);
        Objects.requireNonNull(message);
    }
}
