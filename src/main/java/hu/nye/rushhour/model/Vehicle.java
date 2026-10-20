package hu.nye.rushhour.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Immutable value object: a 1-dimensional vehicle of length L with a fixed orientation.
 * The start position is the top (vertical) or left (horizontal) cell.
 */
public final class Vehicle {

    public static final char HUMAN_HERO_ID = 'A';
    public static final char AI_HERO_ID = 'B';

    private final char id;
    private final Position start;
    private final int length;
    private final Orientation orientation;

    public Vehicle(char id, Position start, int length, Orientation orientation) {
        if (length < 2) {
            throw new IllegalArgumentException("Vehicle length must be at least 2: " + id);
        }
        this.id = id;
        this.start = Objects.requireNonNull(start);
        this.length = length;
        this.orientation = Objects.requireNonNull(orientation);
    }

    public char getId() {
        return id;
    }

    public Position getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Returns the last (bottom or right) cell of the vehicle.
     */
    public Position getEnd() {
        return orientation == Orientation.HORIZONTAL
                ? start.shift(0, length - 1)
                : start.shift(length - 1, 0);
    }

    /**
     * Returns all cells covered by this vehicle.
     */
    public List<Position> getCells() {
        List<Position> cells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            cells.add(orientation == Orientation.HORIZONTAL ? start.shift(0, i) : start.shift(i, 0));
        }
        return List.copyOf(cells);
    }

    public boolean isHero() {
        return id == HUMAN_HERO_ID || id == AI_HERO_ID;
    }

    /**
     * Returns a NEW vehicle moved by the given steps (immutability: this object never changes).
     */
    public Vehicle moved(Direction direction, int steps) {
        Position newStart = start.shift(direction.getRowDelta() * steps, direction.getColumnDelta() * steps);
        return new Vehicle(id, newStart, length, orientation);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vehicle that)) {
            return false;
        }
        return id == that.id && length == that.length
                && start.equals(that.start) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, length, orientation);
    }

    @Override
    public String toString() {
        return "Vehicle{id=" + id + ", start=" + start + ", length=" + length + ", orientation=" + orientation + "}";
    }
}
