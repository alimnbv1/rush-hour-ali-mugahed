package hu.nye.rushhour.service;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Move;
import hu.nye.rushhour.model.Vehicle;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Very simple computer player: picks a random vehicle, and if it has legal moves,
 * picks one of them randomly. Repeats until it finds a vehicle that can move.
 */
public class AiPlayer {

    private final MoveService moveService;
    private final Random random;

    public AiPlayer(MoveService moveService, Random random) {
        this.moveService = moveService;
        this.random = random;
    }

    /**
     * Chooses a move, or returns empty if no vehicle can move at all.
     */
    public Optional<Move> chooseMove(Board board) {
        List<Vehicle> vehicles = board.getVehicles();
        boolean anyMovable = vehicles.stream()
                .anyMatch(vehicle -> !moveService.possibleMoves(board, vehicle).isEmpty());
        if (!anyMovable) {
            return Optional.empty();
        }
        while (true) {
            Vehicle vehicle = vehicles.get(random.nextInt(vehicles.size()));
            List<Move> moves = moveService.possibleMoves(board, vehicle);
            if (!moves.isEmpty()) {
                return Optional.of(moves.get(random.nextInt(moves.size())));
            }
        }
    }
}
