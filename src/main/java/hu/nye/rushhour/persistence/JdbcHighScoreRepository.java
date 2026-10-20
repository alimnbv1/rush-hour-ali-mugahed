package hu.nye.rushhour.persistence;

import hu.nye.rushhour.model.HighScore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC implementation of the high score table (embedded H2 database).
 */
public class JdbcHighScoreRepository implements HighScoreRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcHighScoreRepository.class);

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS high_score ("
            + "player_name VARCHAR(100) PRIMARY KEY, wins INT NOT NULL)";
    private static final String MERGE_WIN = "MERGE INTO high_score h USING (VALUES (CAST(? AS VARCHAR(100)))) "
            + "AS s(name) ON h.player_name = s.name "
            + "WHEN MATCHED THEN UPDATE SET h.wins = h.wins + 1 "
            + "WHEN NOT MATCHED THEN INSERT (player_name, wins) VALUES (s.name, 1)";
    private static final String SELECT_TOP = "SELECT player_name, wins FROM high_score "
            + "ORDER BY wins DESC, player_name ASC LIMIT ?";

    private final String jdbcUrl;

    public JdbcHighScoreRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        createTable();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, "sa", "");
    }

    private void createTable() {
        try (Connection connection = connect(); Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not initialise the database", e);
        }
    }

    @Override
    public void recordWin(String playerName) {
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(MERGE_WIN)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
            LOGGER.info("Win recorded for {}", playerName);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save the win", e);
        }
    }

    @Override
    public List<HighScore> findTop(int limit) {
        List<HighScore> result = new ArrayList<>();
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(SELECT_TOP)) {
            statement.setInt(1, limit);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    result.add(new HighScore(rows.getString("player_name"), rows.getInt("wins")));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read the high scores", e);
        }
        return result;
    }
}
