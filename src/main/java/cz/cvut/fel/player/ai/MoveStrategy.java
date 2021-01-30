package cz.cvut.fel.player.ai;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;

/**
 * Interface for different move strategies.
 *
 * @author frogp
 */
public interface MoveStrategy {

    /**
     * Finds a best move based by used algorithm.
     *
     * @param board current board
     * @return best move
     */
    Move execute(Board board);

}
