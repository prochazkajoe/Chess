package cz.cvut.fel.player.ai;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.eColour;
import cz.cvut.fel.player.MoveMaker;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple implementation of MiniMax Algorithm.
 *
 * @author frogp
 */
public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int SearchDepth;
    
    private static final Logger LOGGER = Logger.getLogger( MiniMax.class.getName() );

    /**
     * Class constructor
     *
     * @param searchDepth maximal depth to search
     */
    public MiniMax(int searchDepth) {
        this.boardEvaluator = new StandartBoardEvaluator();
        this.SearchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer().getColour() + " PLAYER THINKING WITH DEPTH " + this.SearchDepth);

        for (Move move : board.getCurrentPlayer().getValidMoves()) {
            MoveMaker moveMaker = board.getCurrentPlayer().makeMove(move);
            if (moveMaker.getMoveStatus().isDone()) {
                currentValue = ((board.getCurrentPlayer().getColour() == eColour.WHITE)
                        
                        ? min(moveMaker.getBoard(), this.SearchDepth - 1)
                        : max(moveMaker.getBoard(), this.SearchDepth - 1));
                if (board.getCurrentPlayer().getColour() == eColour.WHITE
                        && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;

                } else if (board.getCurrentPlayer().getColour() == eColour.BLACK
                        && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;

        System.out.println("Execution time was: " + executionTime);

        return bestMove;
    }

    /**
     * Co-recurrent function for MiniMax algorithm.
     *
     * @param board current board
     * @param depth current depth
     * @return lowest seen value
     */
    public int min(Board board, int depth) {

        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evalute(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;

        for (Move move : board.getCurrentPlayer().getValidMoves()) {
            MoveMaker moveMaker = board.getCurrentPlayer().makeMove(move);
                

            if (moveMaker.getMoveStatus().isDone()) {
                int currentValue = max(moveMaker.getBoard(), depth - 1);
                LOGGER.log( Level.FINER, "current value[{0}], depth {1}", new Object[]{ currentValue, depth } );

                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    /**
     * Co-recurrent function for MiniMax algorithm.
     *
     * @param board current board
     * @param depth current depth
     * @return highest seen value
     */
    public int max(Board board, int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evalute(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;

        for (Move move : board.getCurrentPlayer().getValidMoves()) {
            MoveMaker moveMaker = board.getCurrentPlayer().makeMove(move);
            if (moveMaker.getMoveStatus().isDone()) {
                int currentValue = min(moveMaker.getBoard(), depth - 1);
                LOGGER.log( Level.FINER, "current value[{0}], depth {1}", new Object[]{ currentValue, depth } );

                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    private static boolean isEndGameScenario(Board board) {
        return board.getCurrentPlayer().isInCheckMate()
                || board.getCurrentPlayer().isInStaleMate();
    }
}
