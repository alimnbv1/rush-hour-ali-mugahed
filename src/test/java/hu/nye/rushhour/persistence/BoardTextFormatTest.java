package hu.nye.rushhour.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Orientation;
import hu.nye.rushhour.model.Position;
import hu.nye.rushhour.model.Vehicle;
import java.util.List;
import org.junit.jupiter.api.Test;

class BoardTextFormatTest {

    private final BoardTextFormat underTest = new BoardTextFormat();

    @Test
    void testParseShouldCreateVehiclesWithCorrectOrientation() throws BoardFormatException {
        // When
        Board result = underTest.parse(List.of("# comment", "AA.C", "...c", "BB.."));
        // Then
        assertEquals(3, result.getRows());
        assertEquals(4, result.getColumns());
        assertEquals(new Vehicle('C', new Position(0, 3), 2, Orientation.VERTICAL), result.getVehicle('C').orElseThrow());
        assertEquals(Orientation.HORIZONTAL, result.getVehicle('A').orElseThrow().getOrientation());
    }

    @Test
    void testFormatShouldBeTheInverseOfParse() throws BoardFormatException {
        List<String> lines = List.of("AA.C", "...C", "BB..");
        assertEquals(lines, underTest.format(underTest.parse(lines)));
    }

    @Test
    void testParseShouldThrowExceptionWhenInputIsInvalid() {
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of()));
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of("AA.", "BB")));
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of("AA1", "BB.")));
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of("AAC", "BB.")));
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of("AAC.", "BBDC", "...C")));
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of("AA.A", "BB..")));
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of("AA..")));
        assertThrows(BoardFormatException.class, () -> underTest.parse(List.of("A...", "A...", "BB..")));
    }

    @Test
    void testDefaultBoardShouldBeSixByTwelve() {
        Board board = DefaultBoard.create();
        assertEquals(6, board.getRows());
        assertEquals(12, board.getColumns());
    }
}
