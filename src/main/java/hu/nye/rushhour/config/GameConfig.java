package hu.nye.rushhour.config;

/**
 * Global, read-only settings of the game.
 * Singleton design pattern (initialization-on-demand holder idiom):
 * there is exactly one instance, created lazily and thread-safely by the JVM.
 */
public final class GameConfig {

    private final String defaultBoardFile = "board.txt";
    private final String defaultSaveFile = "savegame.txt";
    private final String databaseUrl = "jdbc:h2:./rushhour-db";
    private final int highScoreLimit = 10;
    private final String computerName = "Computer";

    private GameConfig() {
    }

    /**
     * Holder class: loaded (and the instance created) only on the first call of {@link #getInstance()}.
     */
    private static final class Holder {
        private static final GameConfig INSTANCE = new GameConfig();
    }

    /**
     * Returns the only instance.
     */
    public static GameConfig getInstance() {
        return Holder.INSTANCE;
    }

    public String getDefaultBoardFile() {
        return defaultBoardFile;
    }

    public String getDefaultSaveFile() {
        return defaultSaveFile;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public int getHighScoreLimit() {
        return highScoreLimit;
    }

    public String getComputerName() {
        return computerName;
    }
}
