package hu.nye.rushhour.persistence;

import hu.nye.rushhour.model.Board;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads and saves boards as text files.
 */
public class BoardFileRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardFileRepository.class);

    private final BoardTextFormat format;

    public BoardFileRepository(BoardTextFormat format) {
        this.format = format;
    }

    /**
     * Reads a board from a text file.
     */
    public Board load(Path path) throws IOException, BoardFormatException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        LOGGER.info("Board loaded from {}", path);
        return format.parse(lines);
    }

    /**
     * Reads the board from the file; if the file is missing or invalid, returns the default board.
     */
    public Board loadOrDefault(Path path) {
        if (path == null || !Files.exists(path)) {
            LOGGER.info("No input file, starting on the default board");
            return DefaultBoard.create();
        }
        try {
            return load(path);
        } catch (IOException | BoardFormatException e) {
            LOGGER.warn("Invalid input file {}: {}. Starting on the default board", path, e.getMessage());
            return DefaultBoard.create();
        }
    }

    /**
     * Writes the current board state into a text file.
     */
    public void save(Path path, Board board) throws IOException {
        Files.write(path, format.format(board), StandardCharsets.UTF_8);
        LOGGER.info("Board saved to {}", path);
    }
}
