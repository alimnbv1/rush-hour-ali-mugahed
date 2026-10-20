package hu.nye.rushhour.ui;

import hu.nye.rushhour.event.GameEventPublisher;
import hu.nye.rushhour.persistence.BoardFileRepository;
import hu.nye.rushhour.persistence.HighScoreRepository;
import hu.nye.rushhour.service.AiPlayer;
import hu.nye.rushhour.service.MoveService;
import hu.nye.rushhour.service.WinChecker;

/**
 * Groups the collaborators of the {@link Game}.
 *
 * @param moveService         move validation and application
 * @param winChecker          terminal state detection
 * @param aiPlayer            computer player
 * @param boardRepository     board file loading / saving
 * @param highScoreRepository high score database
 * @param eventPublisher      publisher of the game events (Observer pattern)
 */
public record GameServices(MoveService moveService, WinChecker winChecker, AiPlayer aiPlayer,
                           BoardFileRepository boardRepository, HighScoreRepository highScoreRepository,
                           GameEventPublisher eventPublisher) {
}
