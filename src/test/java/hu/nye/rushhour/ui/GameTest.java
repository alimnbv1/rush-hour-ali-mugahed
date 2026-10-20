package hu.nye.rushhour.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hu.nye.rushhour.TestBoards;
import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Direction;
import hu.nye.rushhour.model.HighScore;
import hu.nye.rushhour.model.Move;
import hu.nye.rushhour.persistence.BoardFileRepository;
import hu.nye.rushhour.persistence.BoardTextFormat;
import hu.nye.rushhour.persistence.HighScoreRepository;
import hu.nye.rushhour.service.AiPlayer;
import hu.nye.rushhour.service.MoveService;
import hu.nye.rushhour.service.WinChecker;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameTest {

    @Mock
    private AiPlayer aiPlayer;
    @Mock
    private HighScoreRepository highScoreRepository;

    @TempDir
    private Path tempDir;

    private ByteArrayOutputStream output;
    private GameServices services;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        services = new GameServices(new MoveService(), new WinChecker(), aiPlayer,
                new BoardFileRepository(new BoardTextFormat()), highScoreRepository);
    }

    private Game newGame(Board board, String input) {
        return new Game(board, services, new BufferedReader(new StringReader(input)),
                new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    private String output() {
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void testStartShouldRecordWinWhenHumanReachesTheExit() {
        // Given: A can drive out in one move
        Board board = TestBoards.of("AA....", "..BB..");
        when(highScoreRepository.findTop(10)).thenReturn(List.of(new HighScore("Anna", 1)));
        // When
        newGame(board, "Anna\nA R 4\n").start();
        // Then
        verify(highScoreRepository).recordWin("Anna");
        assertTrue(output().contains("You won!"));
        assertTrue(output().contains("Anna"));
    }

    @Test
    void testStartShouldRecordComputerWinWhenAiReachesTheExit() {
        Board board = TestBoards.of("AA...C", ".BB..C");
        when(aiPlayer.chooseMove(any())).thenReturn(Optional.of(new Move('B', Direction.LEFT, 1)));
        newGame(board, "Anna\nC U 0\nC D 5\nA R 1\n").start();
        verify(highScoreRepository).recordWin("Computer");
        assertTrue(output().contains("The computer won!"));
    }

    @Test
    void testHandleShouldKeepBoardWhenMoveIsInvalid() {
        Board board = TestBoards.of("AA.C..", "...C..", "..BB..");
        Game game = newGame(board, "");
        assertTrue(game.handle("A R 3"));
        assertEquals(board, game.getBoard());
        assertTrue(output().contains("Invalid move"));
        verify(aiPlayer, never()).chooseMove(any());
    }

    @Test
    void testHandleShouldApplyHumanAndAiMove() {
        Board board = TestBoards.of("AA.C..", "...C..", "......", "..BB..");
        when(aiPlayer.chooseMove(any())).thenReturn(Optional.of(new Move('C', Direction.DOWN, 1)));
        Game game = newGame(board, "");
        assertTrue(game.handle("A R 1"));
        assertEquals(1, game.getBoard().getVehicle('A').orElseThrow().getStart().getColumn());
        assertEquals(1, game.getBoard().getVehicle('C').orElseThrow().getStart().getRow());
    }

    @Test
    void testHandleShouldReportWhenAiCannotMove() {
        Board board = TestBoards.of("AA.C..", "...C..", "..BB..");
        when(aiPlayer.chooseMove(any())).thenReturn(Optional.empty());
        Game game = newGame(board, "");
        assertTrue(game.handle("A R 1"));
        assertTrue(output().contains("cannot move"));
    }

    @Test
    void testHandleShouldSaveAndLoadBoard() {
        Board board = TestBoards.of("AA.C..", "...C..", "..BB..");
        Game game = newGame(board, "");
        String file = tempDir.resolve("save.txt").toString();
        assertTrue(game.handle("save " + file));
        assertTrue(Files.exists(Path.of(file)));
        assertTrue(game.handle("load " + file));
        assertEquals(board, game.getBoard());
    }

    @Test
    void testHandleShouldReportFileErrors() {
        Game game = newGame(TestBoards.of("AA..", "BB.."), "");
        game.handle("load " + tempDir.resolve("missing.txt"));
        game.handle("save " + tempDir.resolve("no/such/dir/x.txt"));
        assertTrue(output().contains("Could not load"));
        assertTrue(output().contains("Could not save"));
    }

    @Test
    void testHandleShouldProcessHelpScoresQuitAndErrors() {
        Game game = newGame(TestBoards.of("AA..", ".BB."), "");
        assertTrue(game.handle("help"));
        assertTrue(game.handle("scores"));
        assertTrue(game.handle("nonsense"));
        assertFalse(game.handle("quit"));
        assertTrue(output().contains("Commands:"));
        assertTrue(output().contains("no games won yet"));
        assertTrue(output().contains("Error:"));
    }

    @Test
    void testStartShouldUseDefaultNameAndStopAtEndOfInput() {
        newGame(TestBoards.of("AA..", ".BB."), "\n").start();
        assertTrue(output().contains("Hello Player"));
        assertTrue(output().contains("Bye!"));
    }
}
