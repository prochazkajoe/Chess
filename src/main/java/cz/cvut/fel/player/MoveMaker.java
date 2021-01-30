package cz.cvut.fel.player;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;

/**
 * Class for making moves
 *
 * @author frogp
 */
public class MoveMaker {

    private final Board board;
    private final Move move;
    private final MoveStatus status;

    /**
     * Class constructor
     *
     * @param board current board
     * @param move move to make
     * @param status status of move
     */
    public MoveMaker(Board board, Move move, MoveStatus status) {
        this.board = board;
        this.move = move;
        this.status = status;
    }

    /**
     * Returns status of move.
     *
     * @return status of move
     */
    public MoveStatus getMoveStatus() {
        return status;
    }

    /**
     * Returns board after making move.
     *
     * @return board
     */
    public Board getBoard() {
        return board;
    }
}
