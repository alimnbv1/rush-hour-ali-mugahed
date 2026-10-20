package hu.nye.rushhour.model;

import java.util.Objects;

/**
 * Immutable value object: slide the vehicle with the given id in a direction by some steps.
 *
 * @param vehicleId id of the vehicle to move
 * @param direction sliding direction
 * @param steps     number of cells (at least 1, "long move" allowed)
 */
public record Move(char vehicleId, Direction direction, int steps) {

    /**
     * Validating constructor.
     */
    public Move {
        Objects.requireNonNull(direction);
        if (steps < 1) {
            throw new IllegalArgumentException("Steps must be positive");
        }
    }

    @Override
    public String toString() {
        return vehicleId + " " + direction + " " + steps;
    }
}
