package hu.nye.rushhour.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.nye.rushhour.TestBoards;
import hu.nye.rushhour.model.Board;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BoardFileRepositoryTest {

    private final BoardFileRepository underTest = new BoardFileRepository(new BoardTextFormat());

    @TempDir
    private Path tempDir;

    @Test
    void testSaveAndLoadShouldGiveBackTheSameBoard() throws IOException, BoardFormatException {
        // Given
        Board board = TestBoards.of("AA..", "..BB");
        Path file = tempDir.resolve("board.txt");
        // When
        underTest.save(file, board);
        // Then
        assertEquals(board, underTest.load(file));
    }

    @Test
    void testLoadOrDefaultShouldReturnDefaultBoardWhenFileIsMissing() {
        assertEquals(DefaultBoard.create(), underTest.loadOrDefault(tempDir.resolve("missing.txt")));
        assertEquals(DefaultBoard.create(), underTest.loadOrDefault(null));
    }

    @Test
    void testLoadOrDefaultShouldReturnDefaultBoardWhenFileIsInvalid() throws IOException {
        Path file = Files.writeString(tempDir.resolve("bad.txt"), "AA\nB.\n");
        assertEquals(DefaultBoard.create(), underTest.loadOrDefault(file));
    }

    @Test
    void testLoadOrDefaultShouldReadValidFile() throws IOException {
        Path file = Files.writeString(tempDir.resolve("ok.txt"), "AA..\n..BB\n");
        assertEquals(TestBoards.of("AA..", "..BB"), underTest.loadOrDefault(file));
    }

    @Test
    void testLoadShouldThrowExceptionWhenFileIsMissing() {
        assertThrows(IOException.class, () -> underTest.load(tempDir.resolve("none.txt")));
    }
}
