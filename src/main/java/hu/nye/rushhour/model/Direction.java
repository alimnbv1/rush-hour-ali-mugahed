package hu.nye.rushhour.model;

/**
 * Sliding direction of a move.
 */
public enum Direction {
    UP(-1, 0, Orientation.VERTICAL),
    DOWN(1, 0, Orientation.VERTICAL),
    LEFT(0, -1, Orientation.HORIZONTAL),
    RIGHT(0, 1, Orientation.HORIZONTAL);

    private final int rowDelta;
    private final int columnDelta;
    private final Orientation axis;

    Direction(int rowDelta, int columnDelta, Orientation axis) {
        this.rowDelta = rowDelta;
        this.columnDelta = columnDelta;
        this.axis = axis;
    }

    public int getRowDelta() {
        return rowDelta;
    }

    public int getColumnDelta() {
        return columnDelta;
    }

    /**
     * Returns the orientation a vehicle must have to move in this direction.
     */
    public Orientation getAxis() {
        return axis;
    }
}
