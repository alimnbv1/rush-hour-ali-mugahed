package hu.nye.rushhour.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.rushhour.model.HighScore;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JdbcHighScoreRepositoryTest {

    private JdbcHighScoreRepository newRepository() {
        // a fresh in-memory database for every test (FIRST: Independent)
        return new JdbcHighScoreRepository("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
    }

    @Test
    void testRecordWinShouldCountWinsPerPlayer() {
        // Given
        JdbcHighScoreRepository underTest = newRepository();
        // When
        underTest.recordWin("Anna");
        underTest.recordWin("Bela");
        underTest.recordWin("Bela");
        // Then
        assertEquals(List.of(new HighScore("Bela", 2), new HighScore("Anna", 1)), underTest.findTop(10));
    }

    @Test
    void testFindTopShouldRespectTheLimit() {
        JdbcHighScoreRepository underTest = newRepository();
        underTest.recordWin("Anna");
        underTest.recordWin("Bela");
        assertEquals(1, underTest.findTop(1).size());
    }

    @Test
    void testConstructorShouldThrowExceptionWhenDatabaseIsUnreachable() {
        assertThrows(IllegalStateException.class, () -> new JdbcHighScoreRepository("jdbc:invalid:url"));
    }
}
