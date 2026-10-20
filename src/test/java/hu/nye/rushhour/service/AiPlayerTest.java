package hu.nye.rushhour.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import hu.nye.rushhour.TestBoards;
import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Direction;
import hu.nye.rushhour.model.Move;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AiPlayerTest {

    @Mock
    private Random random;

    @Test
    void testChooseMoveShouldRetryUntilItFindsAMovableVehicle() {
        // Given: vehicles sorted A, B, C; A is blocked, B can move left only
        Board board = TestBoards.of(
                "AAC",
                "..C",
                ".BB");
        // first pick A (index 0, blocked), then B (index 1), then its first move
        when(random.nextInt(anyInt())).thenReturn(0, 1, 0);
        AiPlayer underTest = new AiPlayer(new MoveService(), random);
        // When
        Optional<Move> result = underTest.chooseMove(board);
        // Then
        assertEquals(Optional.of(new Move('B', Direction.LEFT, 1)), result);
    }

    @Test
    void testChooseMoveShouldReturnEmptyWhenNothingCanMove() {
        Board board = TestBoards.of("AABB");
        AiPlayer underTest = new AiPlayer(new MoveService(), new Random(1));
        assertTrue(underTest.chooseMove(board).isEmpty());
    }

    @Test
    void testChooseMoveShouldAlwaysReturnALegalMove() {
        Board board = TestBoards.of("..CCC...D..F", ".E......D..F", ".EAA..G.D...",
                "..H...G.BB..", "..H..III....", "JJ.....KK...");
        MoveService moveService = new MoveService();
        AiPlayer underTest = new AiPlayer(moveService, new Random(42));
        for (int i = 0; i < 50; i++) {
            assertTrue(moveService.isValid(board, underTest.chooseMove(board).orElseThrow()));
        }
    }
}
