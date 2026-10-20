package hu.nye.rushhour.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hu.nye.rushhour.TestBoards;
import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Direction;
import hu.nye.rushhour.model.Move;
import hu.nye.rushhour.model.Position;
import hu.nye.rushhour.model.Vehicle;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveServiceTest {

    private MoveService underTest;
    private Board board;

    @BeforeEach
    void setUp() {
        underTest = new MoveService();
        // Given: A can move right 2 (blocked by C), B can move left 1, C is vertical
        board = TestBoards.of(
                "AA..C.",
                "....C.",
                ".BB...");
    }

    @Test
    void testMaxStepsShouldStopAtBlockingVehicle() {
        // When
        int result = underTest.maxSteps(board, board.getVehicle('A').orElseThrow(), Direction.RIGHT);
        // Then
        assertEquals(2, result);
    }

    @Test
    void testMaxStepsShouldStopAtBoardEdge() {
        assertEquals(0, underTest.maxSteps(board, board.getVehicle('A').orElseThrow(), Direction.LEFT));
        assertEquals(1, underTest.maxSteps(board, board.getVehicle('C').orElseThrow(), Direction.DOWN));
    }

    @Test
    void testMaxStepsShouldReturnZeroWhenDirectionIsNotOnTheAxis() {
        assertEquals(0, underTest.maxSteps(board, board.getVehicle('A').orElseThrow(), Direction.UP));
    }

    @Test
    void testApplyShouldMoveVehicleWhenPathIsFree() throws InvalidMoveException {
        // When
        Board result = underTest.apply(board, new Move('A', Direction.RIGHT, 2));
        // Then
        assertEquals(new Position(0, 2), result.getVehicle('A').orElseThrow().getStart());
        // and the original board is unchanged (immutability)
        assertEquals(new Position(0, 0), board.getVehicle('A').orElseThrow().getStart());
    }

    @Test
    void testApplyShouldThrowExceptionWhenPathIsBlocked() {
        assertThrows(InvalidMoveException.class, () -> underTest.apply(board, new Move('A', Direction.RIGHT, 3)));
    }

    @Test
    void testApplyShouldThrowExceptionWhenDirectionIsWrong() {
        assertThrows(InvalidMoveException.class, () -> underTest.apply(board, new Move('A', Direction.DOWN, 1)));
    }

    @Test
    void testApplyShouldThrowExceptionWhenVehicleDoesNotExist() {
        assertThrows(InvalidMoveException.class, () -> underTest.apply(board, new Move('Z', Direction.UP, 1)));
    }

    @Test
    void testIsValidShouldReturnTrueOnlyForLegalMoves() {
        assertTrue(underTest.isValid(board, new Move('B', Direction.LEFT, 1)));
        assertFalse(underTest.isValid(board, new Move('B', Direction.LEFT, 2)));
        assertFalse(underTest.isValid(board, new Move('X', Direction.LEFT, 1)));
    }

    @Test
    void testPossibleMovesShouldListEveryStepCount() {
        // When
        Vehicle hero = board.getVehicle('A').orElseThrow();
        List<Move> result = underTest.possibleMoves(board, hero);
        // Then
        assertEquals(List.of(new Move('A', Direction.RIGHT, 1), new Move('A', Direction.RIGHT, 2)), result);
    }
}
