package hu.nye.rushhour.ui;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.HighScore;
import hu.nye.rushhour.model.Move;
import hu.nye.rushhour.model.PlayerType;
import hu.nye.rushhour.persistence.BoardFileRepository;
import hu.nye.rushhour.persistence.BoardFormatException;
import hu.nye.rushhour.persistence.HighScoreRepository;
import hu.nye.rushhour.service.AiPlayer;
import hu.nye.rushhour.service.InvalidMoveException;
import hu.nye.rushhour.service.MoveService;
import hu.nye.rushhour.service.WinChecker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controls the game flow: reads commands, applies the human move, then lets the AI move,
 * until somebody wins or the player quits.
 */
public class Game {

    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static final int HIGH_SCORE_LIMIT = 10;
    private static final String AI_NAME = "Computer";

    private final MoveService moveService;
    private final WinChecker winChecker;
    private final AiPlayer aiPlayer;
    private final BoardFileRepository boardRepository;
    private final HighScoreRepository highScoreRepository;
    private final CommandParser parser;
    private final BoardRenderer renderer;
    private final BufferedReader in;
    private final PrintStream out;

    private Board board;
    private String playerName;

    /**
     * Creates the game. All collaborators are injected, so they can be mocked in tests.
     */
    public Game(Board board, GameServices services, BufferedReader in, PrintStream out) {
        this.board = board;
        this.moveService = services.moveService();
        this.winChecker = services.winChecker();
        this.aiPlayer = services.aiPlayer();
        this.boardRepository = services.boardRepository();
        this.highScoreRepository = services.highScoreRepository();
        this.parser = new CommandParser();
        this.renderer = new BoardRenderer();
        this.in = in;
        this.out = out;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        out.println("=== RUSH HOUR ===");
        playerName = askName();
        out.println("Hello " + playerName + "! You drive car A to the right exit (>), "
                + "the computer drives car B to the left exit (<).");
        printHelp();
        boolean running = true;
        while (running) {
            out.println(renderer.render(board));
            out.print(playerName + "> ");
            String line = readLine();
            if (line == null) {
                break;
            }
            running = handle(line);
        }
        out.println("Bye!");
    }

    private String askName() {
        out.print("Your name: ");
        String name = readLine();
        return name == null || name.isBlank() ? "Player" : name.strip();
    }

    private String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            LOGGER.error("Could not read input", e);
            return null;
        }
    }

    /**
     * Handles one input line. Returns false if the game is over.
     */
    boolean handle(String line) {
        Command command;
        try {
            command = parser.parse(line);
        } catch (IllegalArgumentException e) {
            out.println("Error: " + e.getMessage());
            return true;
        }
        return switch (command) {
            case Command.QuitCommand quit -> false;
            case Command.HelpCommand help -> {
                printHelp();
                yield true;
            }
            case Command.ScoresCommand scores -> {
                printHighScores();
                yield true;
            }
            case Command.SaveCommand save -> {
                save(save.fileName());
                yield true;
            }
            case Command.LoadCommand load -> {
                load(load.fileName());
                yield true;
            }
            case Command.MoveCommand moveCommand -> playRound(moveCommand.move());
        };
    }

    private boolean playRound(Move humanMove) {
        try {
            board = moveService.apply(board, humanMove);
        } catch (InvalidMoveException e) {
            out.println("Invalid move: " + e.getMessage());
            return true;
        }
        out.println("You moved " + humanMove);
        if (isGameOver()) {
            return false;
        }
        Optional<Move> aiMove = aiPlayer.chooseMove(board);
        if (aiMove.isEmpty()) {
            out.println("The computer cannot move.");
            return true;
        }
        try {
            board = moveService.apply(board, aiMove.get());
            out.println("Computer moved " + aiMove.get());
        } catch (InvalidMoveException e) {
            LOGGER.error("AI produced an invalid move", e);
        }
        return !isGameOver();
    }

    private boolean isGameOver() {
        Optional<PlayerType> winner = winChecker.winner(board);
        if (winner.isEmpty()) {
            return false;
        }
        out.println(renderer.render(board));
        if (winner.get() == PlayerType.HUMAN) {
            out.println("Congratulations " + playerName + ", car A left the board. You won!");
            highScoreRepository.recordWin(playerName);
        } else {
            out.println("Car B left the board. The computer won!");
            highScoreRepository.recordWin(AI_NAME);
        }
        printHighScores();
        return true;
    }

    private void save(String fileName) {
        try {
            boardRepository.save(Path.of(fileName), board);
            out.println("Board saved to " + fileName);
        } catch (IOException e) {
            out.println("Could not save: " + e.getMessage());
        }
    }

    private void load(String fileName) {
        try {
            board = boardRepository.load(Path.of(fileName));
            out.println("Board loaded from " + fileName);
        } catch (IOException | BoardFormatException e) {
            out.println("Could not load: " + e.getMessage());
        }
    }

    private void printHighScores() {
        List<HighScore> scores = highScoreRepository.findTop(HIGH_SCORE_LIMIT);
        out.println("--- HIGH SCORES ---");
        if (scores.isEmpty()) {
            out.println("(no games won yet)");
        }
        for (int i = 0; i < scores.size(); i++) {
            out.printf("%2d. %-20s %d%n", i + 1, scores.get(i).playerName(), scores.get(i).wins());
        }
    }

    private void printHelp() {
        out.println("Commands:");
        out.println("  <car> <U|D|L|R> [steps]  move a car, e.g. 'A R 2' or 'C left'");
        out.println("  save [file]              save the board (default: savegame.txt)");
        out.println("  load [file]              load a board");
        out.println("  scores                   show the high score table");
        out.println("  help                     show this help");
        out.println("  quit                     exit the game");
    }
}
