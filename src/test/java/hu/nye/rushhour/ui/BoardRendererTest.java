package hu.nye.rushhour.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.nye.rushhour.TestBoards;
import org.junit.jupiter.api.Test;

class BoardRendererTest {

    @Test
    void testRenderShouldDrawCoordinatesAndExits() {
        String nl = System.lineSeparator();
        String expected = "     a b c d " + nl
                + " 1 | A A . . >" + nl
                + " 2 < . . B B |" + nl;
        assertEquals(expected, new BoardRenderer().render(TestBoards.of("AA..", "..BB")));
    }
}
