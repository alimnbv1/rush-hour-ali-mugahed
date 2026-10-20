package hu.nye.rushhour.event.listener;

import hu.nye.rushhour.event.GameEvent;
import hu.nye.rushhour.event.GameEventListener;
import hu.nye.rushhour.event.GameEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes every game event into the log file.
 */
public class AuditLoggerListener implements GameEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLoggerListener.class);

    @Override
    public void onEvent(GameEvent event) {
        if (event.type() == GameEventType.ERROR || event.type() == GameEventType.MOVE_REJECTED) {
            LOGGER.warn("[{}] {}", event.type(), event.message());
        } else {
            LOGGER.info("[{}] {}", event.type(), event.message());
        }
    }
}
