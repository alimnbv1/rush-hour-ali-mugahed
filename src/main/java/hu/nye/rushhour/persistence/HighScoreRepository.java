package hu.nye.rushhour.persistence;

import hu.nye.rushhour.model.HighScore;
import java.util.List;

/**
 * Stores how many games each player won.
 */
public interface HighScoreRepository {

    /**
     * Adds one win to the player (creates the player if needed).
     */
    void recordWin(String playerName);

    /**
     * Returns the best players ordered by wins (descending).
     */
    List<HighScore> findTop(int limit);
}
