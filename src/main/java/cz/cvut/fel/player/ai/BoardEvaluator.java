package cz.cvut.fel.player.ai;

import cz.cvut.fel.Board;

/**
 * Interface for board evaluators.
 *
 * @author frogp
 */
public interface BoardEvaluator {

    /**
     * Function for evaluting given board with given depth.
     *
     * @param board board to evalute
     * @param depth depth to search
     * @return value of evaluation
     */
    int evalute(Board board, int depth);
}
