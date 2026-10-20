package hu.nye.rushhour.service;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.PlayerType;
import hu.nye.rushhour.model.Vehicle;
import java.util.Optional;

/**
 * Decides the terminal state. Hero A exits through the right edge, hero B through the left edge.
 * When a hero reaches its exit cell it slides out of the board, so its owner wins.
 */
public class WinChecker {

    /**
     * Returns the winner, or empty if the game is not over yet.
     */
    public Optional<PlayerType> winner(Board board) {
        if (hasExited(board, PlayerType.HUMAN)) {
            return Optional.of(PlayerType.HUMAN);
        }
        if (hasExited(board, PlayerType.AI)) {
            return Optional.of(PlayerType.AI);
        }
        return Optional.empty();
    }

    /**
     * Checks whether the hero of the given player stands on its exit cell.
     */
    public boolean hasExited(Board board, PlayerType player) {
        Optional<Vehicle> hero = board.getVehicle(player.getHeroId());
        if (hero.isEmpty()) {
            return false;
        }
        if (player == PlayerType.HUMAN) {
            return hero.get().getEnd().getColumn() == board.getColumns() - 1;
        }
        return hero.get().getStart().getColumn() == 0;
    }
}
