package cz.cvut.fel.player.ai;

import cz.cvut.fel.Board;
import cz.cvut.fel.pieces.Piece;
import cz.cvut.fel.player.Player;

/**
 * Class for evaluating boards for MiniMax algorithm.
 *
 * @author frogp
 */
public class StandartBoardEvaluator implements BoardEvaluator {

    private final static int CHECK_BONUS = 50;
    private final static int CHECK_MATE_BONUS = 10000;
    private final static int DEPTH_BONUS = 100;
    private final static int CASTLE_BONUS = 60;

    /**
     * Class constructor
     */
    public StandartBoardEvaluator() {
    }

    @Override
    public int evalute(Board board, int depth) {
        return scorePlayer(board, board.getWhitePlayer(), depth)
                - scorePlayer(board, board.getBlackPlayer(), depth);
    }

    private int scorePlayer(Board board, Player player, int depth) {

        return pieceValue(player) + mobility(player) + check(player)
                + checkMate(player, depth) + castled(player);
    }

    private static int pieceValue(Player player) {
        int pieceValueScore = 0;

        for (Piece piece : player.getAlivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }

        return pieceValueScore;
    }

    private int mobility(Player player) {
        return player.getValidMoves().size();
    }

    private static int check(Player player) {
        return (player.getOpponent().isInCheck() ? CHECK_BONUS : 0);
    }

    private static int checkMate(Player player, int depth) {
        return (player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0);
    }

    private static int depthBonus(int depth) {
        return (depth == 0 ? 1 : DEPTH_BONUS * depth);
    }

    private int castled(Player player) {
        return (player.isCastled() ? CASTLE_BONUS : 0);
    }
}
