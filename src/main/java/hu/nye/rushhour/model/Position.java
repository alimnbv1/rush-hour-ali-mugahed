package hu.nye.rushhour.model;

import java.util.Objects;

/**
 * Immutable value object: one cell of the board.
 * Row and column are 0-based internally; the UI shows them as "a1", "b3", ...
 */
public final class Position {

    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Returns a new position shifted by the given amount; this object is not modified.
     */
    public Position shift(int rowDelta, int columnDelta) {
        return new Position(row + rowDelta, column + columnDelta);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position that)) {
            return false;
        }
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "" + (char) ('a' + column) + (row + 1);
    }
}
