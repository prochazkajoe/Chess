package cz.cvut.fel.pieces;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.eBoardUtils;
import cz.cvut.fel.eColour;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for KNIGHT
 *
 * @author frogp
 */
public class Knight extends Piece {

    private final int[] POSSIBLE_MOVE_CANDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    /**
     * Class constructor
     *
     * @param squareID position of knight
     * @param colour colour of knight
     */
    public Knight(int squareID, eColour colour) {
        super(ePieceType.KNIGHT, squareID, colour, true);
    }

    /**
     * Class constructor
     *
     * @param squareID position of knight
     * @param colour colour of knight
     * @param isFirstMove true if is first move, otherwise false
     */
    public Knight(int squareID, eColour colour, boolean isFirstMove) {
        super(ePieceType.KNIGHT, squareID, colour, isFirstMove);
    }

    @Override
    public String toString() {
        return ePieceType.KNIGHT.toString();
    }

    @Override
    public List<Move> findValidMoves(final Board board) {

        List<Move> validMoves = new ArrayList();

        for (int direction : POSSIBLE_MOVE_CANDINATES) {
            if (isFirstColumnExclusion(this.SquareID, direction)
                    || isSecondColumnExclusion(this.SquareID, direction)
                    || isSeventhColumnExclusion(this.SquareID, direction)
                    || isEighthColumnExclusion(this.SquareID, direction)) {
                continue;
            }

            int position = direction + this.getSquareID();

            if (!eBoardUtils.isValidSquareID(position)) {
                continue;
            }
            Square square = board.getSquare(position);
            if (!square.isOccupied()) {
                validMoves.add(new Move.NonAttackMove(board, this, position));
            } else {
                if (square.getPiece().getColour() != this.getColour()) {
                    validMoves.add(new Move.AttackMove(board, this,
                            position, square.getPiece()));
                }
            }
        }
        return validMoves;
    }

    @Override
    public Piece movePiece(Move move) {
        return new Knight((move.getDestination()), colour);
    }

    private static boolean isFirstColumnExclusion(final int curPosition,
            final int direction) {
        return eBoardUtils.INSTANCE.FIRST_COLUMN[curPosition] && ((direction == -17)
                || (direction == -10) || (direction == 6) || (direction == 15));
    }

    private static boolean isSecondColumnExclusion(final int curPosition,
            final int direction) {
        return eBoardUtils.INSTANCE.SECOND_COLUMN[curPosition]
                && ((direction == -10) || (direction == 6));
    }

    private static boolean isSeventhColumnExclusion(final int curPosition,
            final int direction) {
        return eBoardUtils.INSTANCE.SEVENTH_COLUMN[curPosition] && ((direction == -6) || (direction == 10));
    }

    private static boolean isEighthColumnExclusion(final int curPosition,
            final int direction) {
        return eBoardUtils.INSTANCE.EIGHTH_COLUMN[curPosition] && ((direction == -15) || (direction == -6)
                || (direction == 10) || (direction == 17));
    }
}
