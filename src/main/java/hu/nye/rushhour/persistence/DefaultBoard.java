package hu.nye.rushhour.persistence;

import hu.nye.rushhour.model.Board;
import java.util.List;

/**
 * Hard-coded 6 x 12 default board, used when there is no valid input file.
 */
public final class DefaultBoard {

    static final List<String> LINES = List.of(
            "..CCC...D..F",
            ".E......D..F",
            ".EAA..G.D...",
            "..H...G.BB..",
            "..H..III....",
            "JJ.....KK..."
    );

    private DefaultBoard() {
    }

    /**
     * Builds the default board.
     */
    public static Board create() {
        try {
            return new BoardTextFormat().parse(LINES);
        } catch (BoardFormatException e) {
            throw new IllegalStateException("The default board is invalid", e);
        }
    }
}
