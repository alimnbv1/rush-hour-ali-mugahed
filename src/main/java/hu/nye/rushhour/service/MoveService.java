package hu.nye.rushhour.service;

import hu.nye.rushhour.model.Board;
import hu.nye.rushhour.model.Direction;
import hu.nye.rushhour.model.Move;
import hu.nye.rushhour.model.Position;
import hu.nye.rushhour.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business logic of sliding: validates and applies moves.
 * A vehicle can slide along its own axis over any number of free, contiguous cells.
 */
public class MoveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveService.class);

    /**
     * Returns how many cells the vehicle can slide in the given direction (0 if it cannot move).
     */
    public int maxSteps(Board board, Vehicle vehicle, Direction direction) {
        if (vehicle.getOrientation() != direction.getAxis()) {
            return 0;
        }
        Position front = isForward(direction) ? vehicle.getEnd() : vehicle.getStart();
        int steps = 0;
        Position next = front.shift(direction.getRowDelta(), direction.getColumnDelta());
        while (board.isEmpty(next)) {
            steps++;
            next = next.shift(direction.getRowDelta(), direction.getColumnDelta());
        }
        return steps;
    }

    private boolean isForward(Direction direction) {
        return direction == Direction.RIGHT || direction == Direction.DOWN;
    }

    /**
     * Checks whether the move is legal on the given board.
     */
    public boolean isValid(Board board, Move move) {
        return board.getVehicle(move.vehicleId())
                .map(vehicle -> move.steps() <= maxSteps(board, vehicle, move.direction()))
                .orElse(false);
    }

    /**
     * Applies the move and returns the NEW board. The original board is unchanged.
     */
    public Board apply(Board board, Move move) throws InvalidMoveException {
        Vehicle vehicle = board.getVehicle(move.vehicleId())
                .orElseThrow(() -> new InvalidMoveException("There is no vehicle " + move.vehicleId()));
        if (vehicle.getOrientation() != move.direction().getAxis()) {
            throw new InvalidMoveException("Vehicle " + vehicle.getId() + " is "
                    + vehicle.getOrientation().name().toLowerCase() + ", it cannot move "
                    + move.direction().name().toLowerCase());
        }
        int max = maxSteps(board, vehicle, move.direction());
        if (move.steps() > max) {
            throw new InvalidMoveException("Path is blocked: vehicle " + vehicle.getId()
                    + " can move at most " + max + " step(s) " + move.direction().name().toLowerCase());
        }
        LOGGER.debug("Applying move {}", move);
        return board.withVehicle(vehicle.moved(move.direction(), move.steps()));
    }

    /**
     * Lists every legal move of one vehicle.
     */
    public List<Move> possibleMoves(Board board, Vehicle vehicle) {
        List<Move> moves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int max = maxSteps(board, vehicle, direction);
            for (int steps = 1; steps <= max; steps++) {
                moves.add(new Move(vehicle.getId(), direction, steps));
            }
        }
        return moves;
    }
}
