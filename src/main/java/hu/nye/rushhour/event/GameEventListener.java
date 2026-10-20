package hu.nye.rushhour.event;

/**
 * Observer of the Observer design pattern: gets notified about game events.
 */
public interface GameEventListener {

    /**
     * Called by the publisher for every published event.
     */
    void onEvent(GameEvent event);
}
