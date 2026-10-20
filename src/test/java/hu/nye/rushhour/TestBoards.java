package hu.nye.rushhour;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.persistence.BoardFormatException;
import hu.nye.rushhour.persistence.BoardTextFormat;
import java.util.List;

public final class TestBoards {

    private TestBoards() {
    }

    public static Board of(String... lines) {
        try {
            return new BoardTextFormat().parse(List.of(lines));
        } catch (BoardFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
