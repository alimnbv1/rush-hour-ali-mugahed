package hu.nye.rushhour.ui;

import hu.nye.rushhour.model.Direction;
import hu.nye.rushhour.model.Move;

/**
 * Parses command line input into {@link Command} objects.
 * Move syntax: VEHICLE DIRECTION [STEPS], e.g. "A R 2", "c down", "B left 3".
 */
public class CommandParser {

    static final String DEFAULT_SAVE_FILE = "savegame.txt";

    /**
     * Parses one input line.
     *
     * @throws IllegalArgumentException if the input is not a valid command
     */
    public Command parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Empty command");
        }
        String[] parts = input.strip().split("\\s+");
        String keyword = parts[0].toLowerCase();
        return switch (keyword) {
            case "help", "h", "?" -> new Command.HelpCommand();
            case "quit", "exit", "q" -> new Command.QuitCommand();
            case "scores", "highscore", "top" -> new Command.ScoresCommand();
            case "save" -> new Command.SaveCommand(fileName(parts));
            case "load" -> new Command.LoadCommand(fileName(parts));
            default -> new Command.MoveCommand(parseMove(parts));
        };
    }

    private String fileName(String[] parts) {
        return parts.length > 1 ? parts[1] : DEFAULT_SAVE_FILE;
    }

    private Move parseMove(String[] parts) {
        if (parts.length < 2 || parts.length > 3 || parts[0].length() != 1
                || !Character.isLetter(parts[0].charAt(0))) {
            throw new IllegalArgumentException("Unknown command. Type 'help' for the list of commands");
        }
        char vehicleId = Character.toUpperCase(parts[0].charAt(0));
        Direction direction = parseDirection(parts[1]);
        int steps = parts.length == 3 ? parseSteps(parts[2]) : 1;
        return new Move(vehicleId, direction, steps);
    }

    private Direction parseDirection(String text) {
        return switch (text.toLowerCase()) {
            case "u", "up" -> Direction.UP;
            case "d", "down" -> Direction.DOWN;
            case "l", "left" -> Direction.LEFT;
            case "r", "right" -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Unknown direction: " + text + " (use U, D, L or R)");
        };
    }

    private int parseSteps(String text) {
        try {
            int steps = Integer.parseInt(text);
            if (steps < 1) {
                throw new IllegalArgumentException("Steps must be at least 1");
            }
            return steps;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Steps must be a number: " + text);
        }
    }
}
