package hu.nye.rushhour.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable N x M board with the vehicles on it.
 * Hero A (human) exits on the right edge, hero B (AI) exits on the left edge.
 */
public final class Board {

    private final int rows;
    private final int columns;
    private final Map<Character, Vehicle> vehicles;

    public Board(int rows, int columns, List<Vehicle> vehicles) {
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("Board size must be positive");
        }
        this.rows = rows;
        this.columns = columns;
        Map<Character, Vehicle> map = new LinkedHashMap<>();
        for (Vehicle vehicle : vehicles) {
            if (map.put(vehicle.getId(), vehicle) != null) {
                throw new IllegalArgumentException("Duplicate vehicle id: " + vehicle.getId());
            }
        }
        this.vehicles = Map.copyOf(map);
        validate();
    }

    private void validate() {
        Set<Position> occupied = new HashSet<>();
        for (Vehicle vehicle : vehicles.values()) {
            for (Position cell : vehicle.getCells()) {
                if (!isInside(cell)) {
                    throw new IllegalArgumentException("Vehicle " + vehicle.getId() + " is outside of the board");
                }
                if (!occupied.add(cell)) {
                    throw new IllegalArgumentException("Vehicles overlap at " + cell);
                }
            }
        }
        for (char heroId : new char[] {Vehicle.HUMAN_HERO_ID, Vehicle.AI_HERO_ID}) {
            Vehicle hero = vehicles.get(heroId);
            if (hero == null) {
                throw new IllegalArgumentException("Missing hero car: " + heroId);
            }
            if (hero.getOrientation() != Orientation.HORIZONTAL) {
                throw new IllegalArgumentException("Hero car must be horizontal: " + heroId);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     * Returns the vehicles sorted by id.
     */
    public List<Vehicle> getVehicles() {
        List<Vehicle> list = new ArrayList<>(vehicles.values());
        list.sort((a, b) -> Character.compare(a.getId(), b.getId()));
        return List.copyOf(list);
    }

    public Optional<Vehicle> getVehicle(char id) {
        return Optional.ofNullable(vehicles.get(id));
    }

    /**
     * Returns the vehicle covering the given cell, if any.
     */
    public Optional<Vehicle> getVehicleAt(Position position) {
        return vehicles.values().stream()
                .filter(vehicle -> vehicle.getCells().contains(position))
                .findFirst();
    }

    public boolean isInside(Position position) {
        return position.getRow() >= 0 && position.getRow() < rows
                && position.getColumn() >= 0 && position.getColumn() < columns;
    }

    public boolean isEmpty(Position position) {
        return isInside(position) && getVehicleAt(position).isEmpty();
    }

    /**
     * Returns a NEW board where the vehicle with the same id is replaced.
     */
    public Board withVehicle(Vehicle replacement) {
        List<Vehicle> list = new ArrayList<>(vehicles.values());
        list.removeIf(vehicle -> vehicle.getId() == replacement.getId());
        list.add(replacement);
        return new Board(rows, columns, list);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Board that)) {
            return false;
        }
        return rows == that.rows && columns == that.columns && vehicles.equals(that.vehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, columns, vehicles);
    }

    @Override
    public String toString() {
        return "Board{rows=" + rows + ", columns=" + columns + ", vehicles=" + getVehicles() + "}";
    }
}
