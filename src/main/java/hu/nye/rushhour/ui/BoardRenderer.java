package hu.nye.rushhour.ui;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Position;
import hu.nye.rushhour.model.Vehicle;

/**
 * Draws the board as text. Columns are letters (a, b, c, ...), rows are numbers (1, 2, 3, ...).
 * The '>' marks the exit of hero A (right edge), the '<' the exit of hero B (left edge).
 */
public class BoardRenderer {

    /**
     * Renders the board into a printable string.
     */
    public String render(Board board) {
        StringBuilder text = new StringBuilder("     ");
        for (int column = 0; column < board.getColumns(); column++) {
            text.append((char) ('a' + column)).append(' ');
        }
        text.append(System.lineSeparator());
        int heroARow = board.getVehicle(Vehicle.HUMAN_HERO_ID).map(v -> v.getStart().getRow()).orElse(-1);
        int heroBRow = board.getVehicle(Vehicle.AI_HERO_ID).map(v -> v.getStart().getRow()).orElse(-1);
        for (int row = 0; row < board.getRows(); row++) {
            text.append(String.format("%2d ", row + 1));
            text.append(row == heroBRow ? "< " : "| ");
            for (int column = 0; column < board.getColumns(); column++) {
                Position position = new Position(row, column);
                char cell = board.getVehicleAt(position).map(Vehicle::getId).orElse('.');
                text.append(cell).append(' ');
            }
            text.append(row == heroARow ? ">" : "|");
            text.append(System.lineSeparator());
        }
        return text.toString();
    }
}
