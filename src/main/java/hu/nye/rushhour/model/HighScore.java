package hu.nye.rushhour.model;

/**
 * Immutable value object: one row of the high score table.
 *
 * @param playerName name of the player
 * @param wins       number of games won
 */
public record HighScore(String playerName, int wins) {
}
