package hu.nye.rushhour.persistence;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Orientation;
import hu.nye.rushhour.model.Position;
import hu.nye.rushhour.model.Vehicle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a board to / from a simple text grid.
 * Every line is one row, '.' is an empty cell, a letter is a cell of the vehicle with that id.
 * Lines starting with '#' are comments.
 */
public class BoardTextFormat {

    public static final char EMPTY = '.';
    private static final char COMMENT = '#';

    /**
     * Parses the lines of a board file.
     */
    public Board parse(List<String> lines) throws BoardFormatException {
        List<String> grid = lines.stream()
                .map(String::strip)
                .filter(line -> !line.isEmpty() && line.charAt(0) != COMMENT)
                .toList();
        if (grid.isEmpty()) {
            throw new BoardFormatException("The board is empty");
        }
        int columns = grid.get(0).length();
        Map<Character, List<Position>> cellsById = new LinkedHashMap<>();
        for (int row = 0; row < grid.size(); row++) {
            String line = grid.get(row);
            if (line.length() != columns) {
                throw new BoardFormatException("Row " + (row + 1) + " has " + line.length()
                        + " cells instead of " + columns);
            }
            for (int column = 0; column < columns; column++) {
                char cell = line.charAt(column);
                if (cell == EMPTY) {
                    continue;
                }
                if (!Character.isLetter(cell)) {
                    throw new BoardFormatException("Invalid character '" + cell + "' in row " + (row + 1));
                }
                cellsById.computeIfAbsent(Character.toUpperCase(cell), k -> new ArrayList<>())
                        .add(new Position(row, column));
            }
        }
        List<Vehicle> vehicles = new ArrayList<>();
        for (Map.Entry<Character, List<Position>> entry : cellsById.entrySet()) {
            vehicles.add(toVehicle(entry.getKey(), entry.getValue()));
        }
        try {
            return new Board(grid.size(), columns, vehicles);
        } catch (IllegalArgumentException e) {
            throw new BoardFormatException(e.getMessage());
        }
    }

    private Vehicle toVehicle(char id, List<Position> cells) throws BoardFormatException {
        if (cells.size() < 2) {
            throw new BoardFormatException("Vehicle " + id + " must be at least 2 cells long");
        }
        Position first = cells.get(0);
        boolean horizontal = cells.stream().allMatch(cell -> cell.getRow() == first.getRow());
        boolean vertical = cells.stream().allMatch(cell -> cell.getColumn() == first.getColumn());
        Orientation orientation = horizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        if (!horizontal && !vertical) {
            throw new BoardFormatException("Vehicle " + id + " is not in a straight line");
        }
        Vehicle vehicle = new Vehicle(id, first, cells.size(), orientation);
        if (!vehicle.getCells().equals(cells)) {
            throw new BoardFormatException("Vehicle " + id + " is not contiguous");
        }
        return vehicle;
    }

    /**
     * Writes the board into text lines (the same format that {@link #parse(List)} reads).
     */
    public List<String> format(Board board) {
        char[][] grid = new char[board.getRows()][board.getColumns()];
        for (char[] row : grid) {
            java.util.Arrays.fill(row, EMPTY);
        }
        for (Vehicle vehicle : board.getVehicles()) {
            for (Position cell : vehicle.getCells()) {
                grid[cell.getRow()][cell.getColumn()] = vehicle.getId();
            }
        }
        List<String> lines = new ArrayList<>();
        for (char[] row : grid) {
            lines.add(new String(row));
        }
        return lines;
    }
}
