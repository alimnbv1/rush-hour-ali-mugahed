package hu.nye.rushhour.ui;

import hu.nye.rushhour.model.Move;

/**
 * A parsed user command.
 */
public sealed interface Command {

    /**
     * Slide a vehicle, e.g. "A R 2".
     *
     * @param move the move to apply
     */
    record MoveCommand(Move move) implements Command {
    }

    /**
     * Save the current board, e.g. "save my.txt".
     *
     * @param fileName target file
     */
    record SaveCommand(String fileName) implements Command {
    }

    /**
     * Load a board, e.g. "load my.txt".
     *
     * @param fileName source file
     */
    record LoadCommand(String fileName) implements Command {
    }

    /**
     * Show the high score table.
     */
    record ScoresCommand() implements Command {
    }

    /**
     * Show the help.
     */
    record HelpCommand() implements Command {
    }

    /**
     * Quit the game.
     */
    record QuitCommand() implements Command {
    }
}
