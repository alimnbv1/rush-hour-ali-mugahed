package hu.nye.rushhour.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.rushhour.model.Direction;
import hu.nye.rushhour.model.Move;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

class CommandParserTest {

    private final CommandParser underTest = new CommandParser();

    @Test
    void testParseShouldCreateMoveCommandWithSteps() {
        assertEquals(new Command.MoveCommand(new Move('A', Direction.RIGHT, 2)), underTest.parse("a r 2"));
        assertEquals(new Command.MoveCommand(new Move('C', Direction.UP, 1)), underTest.parse(" C up "));
        assertEquals(new Command.MoveCommand(new Move('D', Direction.DOWN, 3)), underTest.parse("D down 3"));
        assertEquals(new Command.MoveCommand(new Move('B', Direction.LEFT, 1)), underTest.parse("B LEFT"));
    }

    @Test
    void testParseShouldRecogniseKeywords() {
        assertEquals(new Command.HelpCommand(), underTest.parse("help"));
        assertEquals(new Command.QuitCommand(), underTest.parse("quit"));
        assertEquals(new Command.ScoresCommand(), underTest.parse("scores"));
        assertEquals(new Command.SaveCommand("x.txt"), underTest.parse("save x.txt"));
        assertEquals(new Command.LoadCommand(CommandParser.DEFAULT_SAVE_FILE), underTest.parse("load"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "hello", "A", "A X", "A R x", "A R 0", "AB R 1", "1 R 1", "A R 1 2"})
    void testParseShouldThrowExceptionWhenInputIsInvalid(String input) {
        assertThrows(IllegalArgumentException.class, () -> underTest.parse(input));
    }

    @Test
    void testParseShouldThrowExceptionWhenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> underTest.parse(null));
    }
}
