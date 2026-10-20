package hu.nye.rushhour;

import hu.nye.rushhour.config.GameConfig;
import hu.nye.rushhour.event.GameEventPublisher;
import hu.nye.rushhour.event.listener.AuditLoggerListener;
import hu.nye.rushhour.event.listener.MoveCounter;
import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.persistence.BoardFileRepository;
import hu.nye.rushhour.persistence.BoardTextFormat;
import hu.nye.rushhour.persistence.JdbcHighScoreRepository;
import hu.nye.rushhour.service.AiPlayer;
import hu.nye.rushhour.service.MoveService;
import hu.nye.rushhour.service.WinChecker;
import hu.nye.rushhour.ui.Game;
import hu.nye.rushhour.ui.GameServices;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Random;

/**
 * Entry point. Only wires the objects together; the game itself runs in {@link Game#start()}.
 */
public final class Main {

    private Main() {
    }

    /**
     * Starts the application. Optional argument: path of the board file.
     */
    public static void main(String[] args) {
        GameConfig config = GameConfig.getInstance();
        Path boardFile = Path.of(args.length > 0 ? args[0] : config.getDefaultBoardFile());

        GameEventPublisher publisher = new GameEventPublisher();
        MoveCounter moveCounter = new MoveCounter();
        publisher.subscribe(new AuditLoggerListener());
        publisher.subscribe(moveCounter);

        MoveService moveService = new MoveService();
        BoardFileRepository boardRepository = new BoardFileRepository(new BoardTextFormat());
        GameServices services = new GameServices(
                moveService,
                new WinChecker(),
                new AiPlayer(moveService, new Random()),
                boardRepository,
                new JdbcHighScoreRepository(config.getDatabaseUrl()),
                publisher);
        Board board = boardRepository.loadOrDefault(boardFile);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        new Game(board, services, in, System.out).start();
        System.out.println("Moves played: " + moveCounter.getAppliedMoves()
                + ", invalid attempts: " + moveCounter.getRejectedMoves());
    }
}
