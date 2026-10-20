package hu.nye.rushhour.event;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameEventPublisherTest {

    @Mock
    private GameEventListener firstListener;
    @Mock
    private GameEventListener secondListener;

    @Test
    void testPublishShouldNotifyEverySubscribedListener() {
        // Given
        GameEventPublisher underTest = new GameEventPublisher();
        underTest.subscribe(firstListener);
        underTest.subscribe(secondListener);
        GameEvent event = new GameEvent(GameEventType.MOVE_APPLIED, "A RIGHT 1");
        // When
        underTest.publish(event);
        // Then
        verify(firstListener).onEvent(event);
        verify(secondListener).onEvent(event);
    }

    @Test
    void testPublishShouldNotNotifyUnsubscribedListener() {
        GameEventPublisher underTest = new GameEventPublisher();
        underTest.subscribe(firstListener);
        underTest.unsubscribe(firstListener);
        GameEvent event = new GameEvent(GameEventType.GAME_STARTED, "x");
        underTest.publish(event);
        verify(firstListener, never()).onEvent(event);
    }
}
