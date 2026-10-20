package hu.nye.rushhour.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject of the Observer design pattern. The game only publishes events;
 * it does not know who listens (loose coupling).
 */
public class GameEventPublisher {

    private final List<GameEventListener> listeners = new ArrayList<>();

    /**
     * Registers a listener.
     */
    public void subscribe(GameEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener.
     */
    public void unsubscribe(GameEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies every registered listener.
     */
    public void publish(GameEvent event) {
        for (GameEventListener listener : List.copyOf(listeners)) {
            listener.onEvent(event);
        }
    }
}
